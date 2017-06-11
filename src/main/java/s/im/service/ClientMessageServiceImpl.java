package s.im.service;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import s.im.connection.client.api.ClientEventCallback;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.client.ClientChatObject;

/**
 * Created by za-zhujun on 2017/6/9.
 */
@Service
public class ClientMessageServiceImpl extends AbstractClientMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessageServiceImpl.class);

    @Override
    public void sendDirectly(ClientEventEnum clientEventEnum, ChatMessageDO chatMessage) {
        final String messageTo = chatMessage.getMessageTo();
        final String messageToBeAck = chatMessage.getMessageId();

        clientMessageSender.send(clientEventEnum, chatMessage, new ClientEventCallback() {
            @Override
            public void onSuccess(ClientChatObject clientChatObject) {
                DateTime dt = new DateTime();
                dt.plusMinutes(1);
                chatMessagePersistService.updateMessageAckTime(messageToBeAck, dt.toDate());
                LOGGER.info("{} 已确认收到消息 {}", messageTo, messageToBeAck);
            }

            @Override
            public void onFailed(ClientChatObject clientChatObject) {

            }

            @Override
            public void onTimeout(ClientChatObject clientChatObject) {

            }
        });
    }
}
