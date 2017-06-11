package s.im.dao;

import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/27.
 */
public interface ChatMessageDAO {

    ChatMessageDO insert(ChatMessageDO chatMessageDO);

    ChatMessageDO find(String messageId);

//    void updateAckTime(String messageId, Date ackTime);

    void updateServerReceiveTime(String messageId, Date time);

    void updateRouteSendTime(String messageId, AddressInfo srcHost, AddressInfo destHost, Date time);

    void updateRouteDestReceiveTime(String messageId, Date time);

    void updateRouteAckTime(String messageId, Date time);

    void updateMessageAckTime(String messageId, Date time);

    void updateRouteNeeded(String messageId, boolean routed);
}
