package s.im.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.dao.ChatMessageDAO;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ChatMessageDOFactory;
import s.im.message.client.ClientChatObject;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/30.
 */
@Component
public class ChatMessageServiceImpl implements ChatMessagePersistService {
    @Autowired
    private ChatMessageDAO chatMessageDAO;

    @Override
    public ChatMessageDO insert(ChatMessageDO chatMessageDO) {
        return chatMessageDAO.insert(chatMessageDO);
    }

    @Override
    public ChatMessageDO find(String messageId) {
        return chatMessageDAO.find(messageId);
    }

    @Override
    public void updateServerReceiveTime(String messageId, Date time) {
        chatMessageDAO.updateServerReceiveTime(messageId, time);
    }

    @Override
    public void updateRouteSendTime(String messageId, AddressInfo srcHost, AddressInfo destHost, Date time) {
        chatMessageDAO.updateRouteSendTime(messageId, srcHost, destHost, time);
    }

    @Override
    public void updateRouteDestReceiveTime(String messageId, Date time) {
        chatMessageDAO.updateRouteDestReceiveTime(messageId, time);
    }

    //    @Override
//    public void updateRouteSendTime(String messageId, Date time) {
//        chatMessageDAO.updateRouteSendTime(messageId, time);
//    }

    @Override
    public void updateRouteAckTime(String messageId, Date time) {
        chatMessageDAO.updateRouteAckTime(messageId, time);
    }

    @Override
    public void updateMessageAckTime(String messageId, Date time) {
        chatMessageDAO.updateMessageAckTime(messageId, time);
    }

    @Override
    public void updateRouteNeeded(String messageId, boolean routed) {
        chatMessageDAO.updateRouteNeeded(messageId, routed);
    }
}
