package s.im.connection.client.api;

import s.im.entity.AddressInfo;
import s.im.message.server.NettyMessage;

/**
 * Created by za-zhujun on 2017/6/9.
 */
public interface ServerDataHandler {
    void onMessage(AddressInfo routeFromAddress, NettyMessage nettyMessage);
}
