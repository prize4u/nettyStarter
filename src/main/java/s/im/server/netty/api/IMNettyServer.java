package s.im.server.netty.api;

import s.im.entity.AddressInfo;
import s.im.entity.NettyServerState;
import s.im.exception.NettyServerException;
import s.im.server.netty.handler.server.HostConnectionRecorder;

import java.util.List;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public interface IMNettyServer extends HostConnectionRecorder {

    void start() throws NettyServerException;

    void startAsyn(NettyOperationCallback callback);

    void stop();

    void restart();

    AddressInfo getAddressInfo();

    boolean isRunning();

    NettyServerState getState();

    void setState(NettyServerState nettyServerState);

    boolean isAcceptedHost(String hostIP);

    boolean addNettyClient(IMNettyClient newNettyClient);

    List<IMNettyClient> getAllNettyClient();

    IMNettyClient getNettyClient(AddressInfo srcHost, AddressInfo destHost);

}
