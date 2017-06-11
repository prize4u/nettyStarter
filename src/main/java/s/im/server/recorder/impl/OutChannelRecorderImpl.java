package s.im.server.recorder.impl;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.server.recorder.api.OutChannelRecorder;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class OutChannelRecorderImpl extends AbstractConnectionRecorder implements OutChannelRecorder {
    private ConcurrentMap<String, Channel> existChannelMap = Maps.newConcurrentMap();

    public OutChannelRecorderImpl(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
    }

    @Override
    public void registOutChannel(AddressInfo remote, Channel channel) {
        existChannelMap.put(buildChannelKey(this.selfAddressInfo, remote), channel);
    }

    @Override
    public void deregistOutChannel(AddressInfo remote, Channel channel) {
        existChannelMap.remove(buildChannelKey(this.selfAddressInfo, remote));
    }

    @Override
    public Channel findOutChannel(AddressInfo remoteAddressInfo) {
        return existChannelMap.get(buildChannelKey(this.selfAddressInfo, remoteAddressInfo));
    }

    protected String buildChannelKey(AddressInfo src, AddressInfo dest) {
        return src + "-->" + dest;
    }
}
