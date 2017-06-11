package s.im.server.netty.api;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.entity.ServerState;
import s.im.exception.IMServerException;
import s.im.server.netty.impl.AbstractStatefulHostAddress;
import s.im.server.recorder.api.OutConnectionChannelRecorder;
import s.im.server.recorder.impl.OutConnectionChannelRecorderImpl;

import java.util.Set;

/**
 * Created by za-zhujun on 2017/5/19.
 */
public abstract class AbstractIMNettyClient extends AbstractStatefulHostAddress implements IMNettyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIMNettyClient.class);
    protected final OutConnectionChannelRecorder outConnectionChannelRecorder;
    protected final AddressInfo remoteAddressInfo;
    protected final IMNettyServer nestNettyServer;

    public AbstractIMNettyClient(AddressInfo addressInfo, AddressInfo remoteAddressInfo, IMNettyServer nestNettyServer) {
        super(addressInfo);
        this.remoteAddressInfo = remoteAddressInfo;
        this.nestNettyServer = nestNettyServer;
        this.outConnectionChannelRecorder = new OutConnectionChannelRecorderImpl(addressInfo);
    }

    @Override
    public IMNettyServer getNestedNettyServer() {
        return this.nestNettyServer;
    }

    @Override
    public void connect() throws IMServerException {
        if (!isRunningOrStarting()) {
            setServerStatus(ServerState.Starting);
            try {
                doConnect();
                setServerStatus(ServerState.Running);
            } catch (Exception e) {
                LOGGER.error(getAddressInfo() + " 连接远程主机 " + this.remoteAddressInfo + " 失败！", e);
                setServerStatus(ServerState.Stopped);
                throw new IMServerException(e);
            }
        } else {
            LOGGER.info("{} 已经连接远程主机 {}", this.getAddressInfo(), this.remoteAddressInfo);
        }
    }

    protected abstract void doConnect();

    protected abstract void doDisConnect();

    private boolean isRunningOrStarting() {
        return ServerState.Running.equals(serverState) || ServerState.Starting.equals(serverState) || ServerState.Restarting.equals(serverState);
    }

    @Override
    public void reconnect() throws IMServerException {
        try {
            // stop if running
            if (isRunning()) {
                LOGGER.info("开始重连：{} --> {}", getAddressInfo(), getConnectingRemoteAddress());
                setServerStatus(ServerState.Restarting);
                doDisConnect();
            }

            // start
            if (!isRunningOrStarting()) {
                doConnect();
                setServerStatus(ServerState.Running);
            }
        } catch (Exception e) {
            LOGGER.error("重连：" + getAddressInfo() + " --> " + getConnectingRemoteAddress() + " 失败！", e);
            setServerStatus(ServerState.Stopped);
            throw new IMServerException(e);
        }
        LOGGER.info("重连：{} --> {} 成功", getAddressInfo(), getConnectingRemoteAddress());
    }

    @Override
    public void disconnect() throws IMServerException {
        if (isRunning()) {
            setServerStatus(ServerState.Stopping);
            try {
                doDisConnect();
                setServerStatus(ServerState.Stopped);
                LOGGER.info("断连：{} --> {} 成功", getAddressInfo(), getConnectingRemoteAddress());
            } catch (Exception e) {
                LOGGER.error("断连：" + getAddressInfo() + " --> " + getConnectingRemoteAddress() + " 失败！", e);
                setServerStatus(ServerState.Stopped);
                throw new IMServerException(e);
            }
        } else {
            LOGGER.info("断连：{} --> {} ， 连接已断开", getAddressInfo(), getConnectingRemoteAddress());
        }
    }


    @Override
    public AddressInfo getConnectingRemoteAddress() {
        return remoteAddressInfo;
    }

    @Override
    public void registOutChannel(AddressInfo remote, Channel channel) {
        outConnectionChannelRecorder.registOutChannel(remote, channel);
        nestNettyServer.registOutChannel(remote, channel);
    }

    @Override
    public void deregistOutChannel(AddressInfo remote, Channel channel) {
        outConnectionChannelRecorder.deregistOutChannel(remote, channel);
        nestNettyServer.deregistOutChannel(remote, channel);
    }

    @Override
    public Channel findOutChannel(AddressInfo remoteAddressInfo) {
        return outConnectionChannelRecorder.findOutChannel(remoteAddressInfo);
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return outConnectionChannelRecorder.getOutcomeRemoteHostDetail();
    }

    @Override
    public void recordOut(AddressInfo remoteAddress) {
        outConnectionChannelRecorder.recordOut(remoteAddress);
        nestNettyServer.recordOut(remoteAddress);
    }

    @Override
    public void removeOut(AddressInfo remoteAddress) {
        outConnectionChannelRecorder.removeOut(remoteAddress);
        nestNettyServer.removeOut(remoteAddress);
    }

    @Override
    public boolean existOutConn(AddressInfo remoteAddress) {
        return outConnectionChannelRecorder.existOutConn(remoteAddress);
    }
}
