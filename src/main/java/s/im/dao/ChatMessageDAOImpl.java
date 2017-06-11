package s.im.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ChatMessageDOConstant;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/27.
 */
@Component
public class ChatMessageDAOImpl implements ChatMessageDAO {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${mongo.chatmessage.collectionName}")
    private String chatHisotryCollectionName;

    @Override
    public ChatMessageDO insert(ChatMessageDO chatMessageDO) {
        mongoTemplate.insert(chatMessageDO, chatHisotryCollectionName);
        return chatMessageDO;
    }

    @Override
    public ChatMessageDO find(String messageId) {
        return mongoTemplate.findOne(Query.query(Criteria.where(ChatMessageDOConstant.MESSAGE_ID).is(messageId))
                , ChatMessageDO.class
                , chatHisotryCollectionName);
    }

    @Override
    public void updateServerReceiveTime(String messageId, Date time) {
        updateTime(messageId, ChatMessageDOConstant.SERVER_RECEIVE_TIME, time);
    }

    @Override
    public void updateRouteSendTime(String messageId, AddressInfo srcHost, AddressInfo destHost, Date time) {
        mongoTemplate.updateMulti(Query.query(Criteria.where(ChatMessageDOConstant.MESSAGE_ID).is(messageId))
                , Update.update(ChatMessageDOConstant.ROUTE_SRC_SEND_TIME, time)
                        .set(ChatMessageDOConstant.ROUTE_SRC_HOST, srcHost.getIpAddress())
                        .set(ChatMessageDOConstant.ROUTE_DEST_HOST, destHost.getIpAddress())
                        .set(ChatMessageDOConstant.ROUTE_NEEDED, true)
                        .set(ChatMessageDOConstant.GMT_MODIFIED, new Date())
                , chatHisotryCollectionName);

        updateTime(messageId, ChatMessageDOConstant.ROUTE_SRC_SEND_TIME, time);
    }

    @Override
    public void updateRouteDestReceiveTime(String messageId, Date time) {
        updateTime(messageId, ChatMessageDOConstant.ROUTE_DEST_RCV_TIME, time);
    }

    @Override
    public void updateRouteAckTime(String messageId, Date time) {
        updateTime(messageId, ChatMessageDOConstant.ROUTE_DESC_ACK_TIME, time);
    }

    @Override
    public void updateMessageAckTime(String messageId, Date time) {
        updateTime(messageId, ChatMessageDOConstant.MESSAGE_ACK_TIME, time);
    }

    @Override
    public void updateRouteNeeded(String messageId, boolean routed) {
        mongoTemplate.updateMulti(Query.query(Criteria.where(ChatMessageDOConstant.MESSAGE_ID).is(messageId))
                , Update.update(ChatMessageDOConstant.ROUTE_NEEDED, routed)
                , chatHisotryCollectionName);
    }

    private void updateTime(String messageId, String columnName, Date ackTime) {
        mongoTemplate.updateMulti(Query.query(Criteria.where(ChatMessageDOConstant.MESSAGE_ID).is(messageId))
                , Update.update(columnName, ackTime).set(ChatMessageDOConstant.GMT_MODIFIED, new Date())
                , chatHisotryCollectionName);
    }
}
