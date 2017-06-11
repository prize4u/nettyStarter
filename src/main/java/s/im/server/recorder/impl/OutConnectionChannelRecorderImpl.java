package s.im.server.recorder.impl;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.recorder.api.OutConnectionChannelRecorder;
import s.im.server.recorder.api.OutConnectionRecorder;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class OutConnectionChannelRecorderImpl extends AbstractConnectionRecorder implements OutConnectionChannelRecorder {
    private final OutConnectionRecorder outConnectionRecorder;
    private ConcurrentHashMap<String, Channel> outChannelMap = new ConcurrentHashMap<>();

    public OutConnectionChannelRecorderImpl(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
        this.outConnectionRecorder = new OutConnectionRecorderImpl(selfAddressInfo);
    }

    private String buildKey(AddressInfo remoteHost) {
        return selfAddressInfo + "-->" + remoteHost;
    }

    @Override
    public void registOutChannel(AddressInfo remote, Channel channel) {
        outChannelMap.put(buildKey(remote), channel);
        outConnectionRecorder.recordOut(remote);
    }

    @Override
    public void deregistOutChannel(AddressInfo remote, Channel channel) {
        outChannelMap.remove(buildKey(remote));
        outConnectionRecorder.removeOut(remote);
    }

    @Override
    public Channel findOutChannel(AddressInfo remoteAddressInfo) {
        return outChannelMap.get(buildKey(remoteAddressInfo));
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return outConnectionRecorder.getOutcomeRemoteHostDetail();
    }

    @Override
    public void recordOut(AddressInfo remoteAddress) {
        outConnectionRecorder.recordOut(remoteAddress);
    }

    @Override
    public void removeOut(AddressInfo remoteAddress) {
        outConnectionRecorder.removeOut(remoteAddress);
    }

    @Override
    public boolean existOutConn(AddressInfo remoteAddress) {
        return outConnectionRecorder.existOutConn(remoteAddress);
    }
}
