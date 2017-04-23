package s.im.server.netty.api;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.NettyServerConnectState;
import s.im.server.netty.handler.server.OutcomeConnectionRecorder;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public interface IMNettyClient extends OutcomeConnectionRecorder {

    void connect();

    void reconnect();

    void disconnect();

    void disconnectAsync(NettyOperationCallback callback);

    void shutdown();

    Channel getChannel();

    AddressInfo getSelfAddressInfo();

    AddressInfo getServerAddressInfo();

    boolean isConnected();

    NettyServerConnectState getState();

    void setState(NettyServerConnectState state);

}
