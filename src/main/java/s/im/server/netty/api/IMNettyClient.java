package s.im.server.netty.api;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.exception.IMServerException;
import s.im.server.api.HostAddressable;
import s.im.server.api.ServerStatusTrackable;
import s.im.server.recorder.api.OutConnectionChannelRecorder;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public interface IMNettyClient extends OutConnectionChannelRecorder, HostAddressable, ServerStatusTrackable {

    void connect() throws IMServerException;

    void reconnect() throws IMServerException;

    void disconnect() throws IMServerException;

//    void disconnectAsync(NettyOperationCallback callback);

    void shutdown();

//    boolean isConnected();

    AddressInfo getConnectingRemoteAddress();

    IMNettyServer getNestedNettyServer();

    Channel getChannel();

}
