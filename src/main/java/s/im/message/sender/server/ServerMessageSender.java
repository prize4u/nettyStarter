package s.im.message.sender.server;

import s.im.entity.AddressInfo;
import s.im.message.sender.MessageSender;
import s.im.message.server.NettyMessage;

/**
 * Created by za-zhujun on 2017/6/7.
 */
public interface ServerMessageSender extends MessageSender {
    void send(AddressInfo remoteAddress, NettyMessage nettyMessage);
}
