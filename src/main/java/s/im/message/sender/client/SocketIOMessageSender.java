package s.im.message.sender.client;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.SocketIOConnection;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.connection.client.api.ClientEventCallback;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ChatMessageDOFactory;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.client.ClientChatObject;
import s.im.server.recorder.api.ClientConnectionRecorder;

/**
 * Created by za-zhujun on 2017/6/7.
 */
@Component
public class SocketIOMessageSender implements ClientMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIOMessageSender.class);
    @Autowired
    private ClientConnectionRecorder clientConnectionRecorder;

    @Override
    public void send(ClientEventEnum clientEventEnum, ChatMessageDO chatMessage, ClientEventCallback callback) {
        Preconditions.checkArgument(chatMessage != null, "消息为空");
        final String messageTo = chatMessage.getMessageTo();
        ClientConnectionChannel socketIOClient = clientConnectionRecorder.findClient(messageTo);

        ClientChatObject clientChatObject = ChatMessageDOFactory.toClientChatObject(chatMessage);
        final String messageToBeAck = clientChatObject.getMessageId();
//        LOGGER.info("发送消息{} --> {} , 消息ID：{}， 消息内容：{}", chatMessage.getMessageFrom(), messageTo, messageToBeAck, clientChatObject.getMessage());
        LOGGER.info("发送消息{} --> {}, 使用client hashcode: {}", chatMessage.getMessageFrom(), messageTo, (
                (SocketIOConnection)socketIOClient).getClient().hashCode());
        socketIOClient.send(clientEventEnum, clientChatObject, callback);
//        socketIOClient.persistAndSend(clientEventEnum, clientChatObject, new ClientEventCallback() {
//            @Override
//            public void onSuccess(ClientChatObject clientChatObject) {
////                String messageToBeAck = clientChatObject.getMessageId();
//                if (clientChatObject != null && StringUtils.equals(messageToBeAck, (String) clientChatObject.getMessage())) {
//                    DateTime dt = new DateTime();
//                    dt.plusMinutes(1);
//                    chatMessagePersistService.updateAckTime(messageToBeAck, dt.toDate());
//                    LOGGER.info("{} 已确认收到消息 {}", messageTo, messageToBeAck);
//                } else {
//                    LOGGER.info("{} 未收到确认收到消息 {}", messageTo, messageToBeAck);
//                    // TODO: retry
//                }
//            }
//
//            @Override
//            public void onFailed(ClientChatObject clientChatObject) {
//
//            }
//
//            @Override
//            public void onTimeout(ClientChatObject clientChatObject) {
//
//            }
//        });
    }
}
