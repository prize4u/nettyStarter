package s.im.message.sender.client;

import s.im.connection.client.api.ClientEventCallback;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.sender.MessageSender;

/**
 * Created by za-zhujun on 2017/6/7.
 */
public interface ClientMessageSender extends MessageSender {
    void send(ClientEventEnum clientEventEnum, ChatMessageDO chatMessage, ClientEventCallback callback);
}
