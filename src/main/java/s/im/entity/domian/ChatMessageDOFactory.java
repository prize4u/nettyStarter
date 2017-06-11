package s.im.entity.domian;

import s.im.message.client.ClientChatObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by za-zhujun on 2017/5/30.
 */
public class ChatMessageDOFactory {

    public static ChatMessageDO buildS2CGreetingMessage(String from, String to) {
        ChatMessageDO object = new ChatMessageDO();
        object.setMessageId(UUID.randomUUID().toString());
        object.setMessageFrom(from);
        object.setMessageTo(to);
        object.setContent("你好，" + from + " 为您服务");
        object.setContentType(ChatMessageContentTypeEnum.TEXT.getCode());
        object.setClientSendTime(new Date());
        object.setRouteNeeded(false);
        object.setGmtCreated(new Date());
        object.setGmtModified(new Date());
        return object;
    }

    public static ChatMessageDO buildClientGreetingMessage(String messageTo) {
        ChatMessageDO object = new ChatMessageDO();
        object.setMessageId(UUID.randomUUID().toString());
        object.setMessageFrom("system");
        object.setMessageTo(messageTo);
        object.setContent("你好，欢迎登陆");
        object.setContentType(ChatMessageContentTypeEnum.TEXT.getCode());
        object.setClientSendTime(new Date());
        object.setRouteNeeded(false);
        object.setGmtCreated(new Date());
        object.setGmtModified(new Date());
        return object;
    }

//    public static ChatMessageDO buildServantGreetingMessage() {
//        ClientChatObject<String> object = new ClientChatObject();
//        object.setMessageId(UUID.randomUUID().toString());
//        object.setMessageType(MessageType.SERVER_TO_CLIENT_CHAT_REQ.value());
//        object.setMessage("你好，欢迎登陆客服系统");
//        object.setSendDate(new Date().getTime());
//        object.setUserName("system");
//        return object;
//    }

    public static ChatMessageDO newChatMessageDO(String from
            , String to
            , String sessionId
            , ClientChatObject clientChatObject) {
        ChatMessageDO chatMessageDO = new ChatMessageDO();
        chatMessageDO.setMessageId(clientChatObject.getMessageId());
        chatMessageDO.setMessageFrom(from);
        chatMessageDO.setMessageTo(to);
        chatMessageDO.setContent(clientChatObject.getMessage());
        chatMessageDO.setContentType(ChatMessageContentTypeEnum.TEXT.getCode());
        chatMessageDO.setClientSendTime(new Date(clientChatObject.getSendDate()));
        chatMessageDO.setServerReceiveTime(new Date());
        chatMessageDO.setSessionId(sessionId);
        chatMessageDO.setGmtCreated(new Date());
        chatMessageDO.setGmtModified(new Date());
        return chatMessageDO;
    }

    public static ClientChatObject toClientChatObject(ChatMessageDO chatMessageDO) {
        ClientChatObject clientChatObject = new ClientChatObject();
        clientChatObject.setSendDate(chatMessageDO.getClientSendTime().getTime());
        clientChatObject.setUserName(chatMessageDO.getMessageFrom());
        clientChatObject.setMessage(chatMessageDO.getContent());
        clientChatObject.setMessageId(chatMessageDO.getMessageId());
        return clientChatObject;
    }
}
