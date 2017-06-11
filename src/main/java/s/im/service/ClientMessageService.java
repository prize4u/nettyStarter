package s.im.service;

import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ClientEventEnum;

/**
 * Created by za-zhujun on 2017/6/9.
 */
public interface ClientMessageService {
    /**
     * persistAndSend message to client on event type, message is persisted before persistAndSend out
     *
     * @param clientEventEnum
     * @param chatMessage
     */
    void persistAndSend(ClientEventEnum clientEventEnum, ChatMessageDO chatMessage);

    /**
     * persistAndSend message to client on event type without persisting
     *
     * @param clientEventEnum
     * @param chatMessage
     */
    void sendDirectly(ClientEventEnum clientEventEnum, ChatMessageDO chatMessage);


    void persist(ChatMessageDO chatMessage);
}
