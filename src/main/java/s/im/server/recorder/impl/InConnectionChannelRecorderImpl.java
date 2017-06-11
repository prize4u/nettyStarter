package s.im.server.recorder.impl;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.message.sender.server.NettyMessageSender;
import s.im.server.recorder.api.InConnectionChannelRecorder;
import s.im.server.recorder.api.InConnectionRecorder;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class InConnectionChannelRecorderImpl extends AbstractConnectionRecorder implements InConnectionChannelRecorder {
    private static final Logger LOGGER = LoggerFactory.getLogger(InConnectionChannelRecorderImpl.class);
    private InConnectionRecorder inConnectionRecorder;
    private ConcurrentHashMap<String, Channel> inChannelMap = new ConcurrentHashMap<>();

    public InConnectionChannelRecorderImpl(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
        this.inConnectionRecorder = new InConnectionRecorderImpl(selfAddressInfo);
    }

    private String buildKey(AddressInfo remoteHost) {
        return remoteHost + "-->" + selfAddressInfo;
    }

    @Override
    public void registInChannel(AddressInfo remote, Channel channel) {
        String channelKey = buildKey(remote);
        LOGGER.info("记录新连接：{}， hash：{}", channelKey, channel.hashCode());
        inChannelMap.put(channelKey, channel);
        recordIn(remote);
    }

    @Override
    public void deregistInChannel(AddressInfo remote, Channel channel) {
        inChannelMap.remove(buildKey(remote));
        removeIn(remote);
    }

    @Override
    public Channel findInChannel(AddressInfo remoteAddressInfo) {
        return inChannelMap.get(buildKey(remoteAddressInfo));
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return inConnectionRecorder.getIncomeRemoteHostDetail();
    }

    @Override
    public void recordIn(AddressInfo remoteAddress) {
        inConnectionRecorder.recordIn(remoteAddress);
    }

    @Override
    public void removeIn(AddressInfo remoteAddress) {
        inConnectionRecorder.removeIn(remoteAddress);
    }

    @Override
    public boolean existInConn(AddressInfo remoteAddress) {
        return inConnectionRecorder.existInConn(remoteAddress);
    }
}
