package s.im.service.impl;

import com.google.common.collect.Maps;
import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.service.api.ChannelRegistor;
import s.im.utils.ChannelUtils;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by za-zhujun on 2017/4/24.
 */
@Component
public class ChannelRegistorImpl implements ChannelRegistor {
    private ConcurrentMap<String, Channel> existChannelMap = Maps.newConcurrentMap();
    private ConcurrentMap<String, Condition> waitingConditionMap = Maps.newConcurrentMap();
    private Lock lock = new ReentrantLock(true);

    @Override
    public boolean registChannel(AddressInfo src, AddressInfo remote, Channel channel) {
        String channelKey = buildChannelKey(src, remote);
        Channel _exist = existChannelMap.putIfAbsent(channelKey, channel);

//        if (waitingConditionMap.containsKey(channelKey)) {
//            Condition condition = waitingConditionMap.get(channelKey);
//            // wake up all waiting thread
//            try {
//                lock.lock();
//                condition.notifyAll();
//            } finally {
//                lock.unlock();
//            }
//        }
        return _exist == null;
    }

    private String buildChannelKey(AddressInfo src, AddressInfo remote) {
        return src + "-->" + remote;
    }

    @Override
    public boolean deregistChannel(AddressInfo src, AddressInfo remote, Channel channel) {
        String channelKey = buildChannelKey(src, remote);
        Channel existChannel = existChannelMap.remove(channelKey);
        return existChannel != null;
    }

    @Override
    public Channel find(AddressInfo src, AddressInfo remote) {
        String channelKey = buildChannelKey(src, remote);
        Channel channel = existChannelMap.get(channelKey);
//        if (!ChannelUtils.isOpenAndActive(channel)) {
//            waitingChannelReady(channelKey);
//            channel = existChannelMap.get(channelKey);
//        }
        while (ChannelUtils.isOpenAndActive(channel)) {
            try {
                TimeUnit.SECONDS.sleep(5);
                channel = existChannelMap.get(channelKey);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return channel;
    }

    private void waitingChannelReady(String channelKey) {
        // wait for underlying netty to re-connect
        try {
            lock.lock();
            Condition condition = waitingConditionMap.get(channelKey);
            if (condition == null) {
                condition = lock.newCondition();
                Condition _condition = waitingConditionMap.putIfAbsent(channelKey, condition);
                if (_condition != null) {
                    condition = _condition;
                }
            }
            condition.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }
}
