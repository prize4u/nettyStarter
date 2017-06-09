package s.im.message.client;

import s.im.message.MessageType;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by za-zhujun on 2017/3/28.
 */
public class ChatObjectFactory {
    public static ClientChatObject buildS2CGreetingMessage(String servantName) {
        ClientChatObject<String> object = new ClientChatObject();
        object.setMessageId(UUID.randomUUID().toString());
        object.setMessageType(MessageType.SERVER_TO_CLIENT_CHAT_REQ.value());
        object.setMessage("尊敬的客户你好, " + servantName + " 为您服务");
        object.setSendDate(new Date().getTime());
        object.setUserName(servantName);
        return object;
    }

    public static ClientChatObject buildClientGreetingMessage() {
        ClientChatObject<String> object = new ClientChatObject();
        object.setMessageId(UUID.randomUUID().toString());
        object.setMessageType(MessageType.SERVER_TO_CLIENT_CHAT_REQ.value());
        object.setMessage("尊敬的客户你好，欢迎访问众安客服系统");
        object.setSendDate(new Date().getTime());
        object.setUserName("system");
        return object;
    }

    public static ClientChatObject buildServantGreetingMessage() {
        ClientChatObject<String> object = new ClientChatObject();
        object.setMessageId(UUID.randomUUID().toString());
        object.setMessageType(MessageType.SERVER_TO_CLIENT_CHAT_REQ.value());
        object.setMessage("你好，欢迎登陆客服系统");
        object.setSendDate(new Date().getTime());
        object.setUserName("system");
        return object;
    }

    public static ClientAckMessage generateAckClientMessage(String ackMessageId) {
        ClientAckMessage ack = new ClientAckMessage();
        ack.setSendDate(new Date().getTime());
        ack.setAckMessageId(ackMessageId);
        ack.setMessageType(MessageType.SERVER_TO_CLIENT_ACK.value());
        ack.setUserName("system");
        return ack;
    }
}
