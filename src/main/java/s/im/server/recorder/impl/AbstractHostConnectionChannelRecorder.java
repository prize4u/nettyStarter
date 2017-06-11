package s.im.server.recorder.impl;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.recorder.api.*;

import java.util.Set;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class AbstractHostConnectionChannelRecorder extends AbstractConnectionRecorder implements HostConnectionChannelRecorder {
    protected final InConnectionChannelRecorder inConnectionChannelRecorder;
    protected final OutConnectionChannelRecorder outConnectionChannelRecorder;
    protected final InChannelRecorder inChannelRecorder;
    protected final OutChannelRecorder outChannelRecorder;

    public AbstractHostConnectionChannelRecorder(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
        this.inConnectionChannelRecorder = new InConnectionChannelRecorderImpl(selfAddressInfo);
        this.outConnectionChannelRecorder = new OutConnectionChannelRecorderImpl(selfAddressInfo);
        this.inChannelRecorder = new InChannelRecorderImpl(selfAddressInfo);
        this.outChannelRecorder = new OutChannelRecorderImpl(selfAddressInfo);
    }

    @Override
    public void registInChannel(AddressInfo remote, Channel channel) {
        inChannelRecorder.registInChannel(remote, channel);
    }

    @Override
    public void registOutChannel(AddressInfo remote, Channel channel) {
        outChannelRecorder.registOutChannel(remote, channel);
    }

    @Override
    public void deregistInChannel(AddressInfo remote, Channel channel) {
        inChannelRecorder.deregistInChannel(remote, channel);
    }

    @Override
    public void deregistOutChannel(AddressInfo remote, Channel channel) {
        outChannelRecorder.deregistOutChannel(remote, channel);
    }

    @Override
    public Channel findInChannel(AddressInfo remoteAddressInfo) {
        return inChannelRecorder.findInChannel(remoteAddressInfo);
    }

    @Override
    public Channel findOutChannel(AddressInfo remoteAddressInfo) {
        return outChannelRecorder.findOutChannel(remoteAddressInfo);
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return outConnectionChannelRecorder.getOutcomeRemoteHostDetail();
    }

    @Override
    public void recordOut(AddressInfo remoteAddress) {
        outConnectionChannelRecorder.recordOut(remoteAddress);
    }

    @Override
    public void removeOut(AddressInfo remoteAddress) {
        outConnectionChannelRecorder.removeOut(remoteAddress);
    }

    @Override
    public boolean existOutConn(AddressInfo remoteAddress) {
        return outConnectionChannelRecorder.existOutConn(remoteAddress);
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return inConnectionChannelRecorder.getIncomeRemoteHostDetail();
    }

    @Override
    public void recordIn(AddressInfo remoteAddress) {
        inConnectionChannelRecorder.removeIn(remoteAddress);
    }

    @Override
    public void removeIn(AddressInfo remoteAddress) {
        inConnectionChannelRecorder.removeIn(remoteAddress);
    }

    @Override
    public boolean existInConn(AddressInfo remoteAddress) {
        return inConnectionChannelRecorder.existInConn(remoteAddress);
    }
}
