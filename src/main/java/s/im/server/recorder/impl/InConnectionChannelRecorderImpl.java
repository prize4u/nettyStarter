package s.im.server.recorder.impl;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.recorder.api.InConnectionChannelRecorder;
import s.im.server.recorder.api.InConnectionRecorder;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class InConnectionChannelRecorderImpl extends AbstractConnectionRecorder implements InConnectionChannelRecorder {
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
        inChannelMap.put(buildKey(remote), channel);
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
