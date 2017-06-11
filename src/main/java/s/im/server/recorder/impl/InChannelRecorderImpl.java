package s.im.server.recorder.impl;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.recorder.api.InChannelRecorder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class InChannelRecorderImpl extends AbstractConnectionRecorder implements InChannelRecorder {
    private ConcurrentHashMap<String, Channel> incomeConnectionsMap = new ConcurrentHashMap<>();

    public InChannelRecorderImpl(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
    }

    @Override
    public void registInChannel(AddressInfo remote, Channel channel) {
        incomeConnectionsMap.put(buildChannelKey(remote, this.selfAddressInfo), channel);
    }

    @Override
    public void deregistInChannel(AddressInfo remote, Channel channel) {
        incomeConnectionsMap.remove(buildChannelKey(remote, this.selfAddressInfo));
    }

    @Override
    public Channel findInChannel(AddressInfo remoteAddressInfo) {
        return incomeConnectionsMap.get(buildChannelKey(remoteAddressInfo, this.selfAddressInfo));
    }

    protected String buildChannelKey(AddressInfo src, AddressInfo dest) {
        return src + "-->" + dest;
    }
}
