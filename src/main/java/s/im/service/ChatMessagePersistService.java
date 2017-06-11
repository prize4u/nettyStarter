package s.im.service;

import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;
import s.im.message.client.ClientChatObject;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/30.
 */
public interface ChatMessagePersistService {
//    ChatMessageDO insert(String from, String to, String sessionId, ClientChatObject clientChatObject);

    ChatMessageDO insert(ChatMessageDO chatMessageDO);

    ChatMessageDO find(String messageId);

    void updateRouteNeeded(String messageId, boolean routed);

    void updateServerReceiveTime(String messageId, Date time);

    void updateRouteSendTime(String messageId, AddressInfo srcHost, AddressInfo destHost, Date time);

    void updateRouteDestReceiveTime(String messageId, Date time);

    void updateRouteAckTime(String messageId, Date time);

    void updateMessageAckTime(String messageId, Date time);
}
