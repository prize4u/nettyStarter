package s.im.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.sender.client.ClientMessageSender;

/**
 * Created by za-zhujun on 2017/6/9.
 */
public abstract class AbstractClientMessageService implements ClientMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClientMessageService.class);
    @Autowired
    protected ChatMessagePersistService chatMessagePersistService;
    @Autowired
    protected ClientMessageSender clientMessageSender;


    @Override
    public void persistAndSend(ClientEventEnum clientEventEnum, ChatMessageDO chatMessage) {
        persist(chatMessage);
        sendDirectly(clientEventEnum, chatMessage);
    }

    @Override
    public void persist(ChatMessageDO chatMessage) {
        chatMessagePersistService.insert(chatMessage);
    }
}
