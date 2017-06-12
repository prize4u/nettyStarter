package s.im.message.server;

import com.google.common.collect.Maps;
import s.im.entity.domian.ChatMessageDO;
import s.im.message.MessageType;
import s.im.util.Constant;

import java.util.Map;
import java.util.UUID;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public class NettyMessageFactory {

    public static NettyMessage newHeartBeanReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        header.setMessageId(UUID.randomUUID().toString());
        message.setHeader(header);
        return message;
    }

    public static NettyMessage newHeartBeanResp() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        header.setMessageId(UUID.randomUUID().toString());
        message.setHeader(header);
        return message;
    }

    public static NettyMessage newLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        header.setMessageId(UUID.randomUUID().toString());
        message.setHeader(header);
        return message;
    }

    public static NettyMessage newLoginResp(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        header.setMessageId(UUID.randomUUID().toString());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    public static NettyMessage newServiceResp(String messageId) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.SERVICE_RESP.value());
        header.setMessageId(UUID.randomUUID().toString());
        message.setHeader(header);
        message.setBody(messageId);
        return message;
    }

    public static NettyMessage clientMessageToNettyMessage(ChatMessageDO chatMessageDO) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.SERVICE_REQ.value());
        header.setMessageId(UUID.randomUUID().toString());
        Map<String, Object> attachment = Maps.newHashMap();
//        attachment.put(Constant.LOGIN_USER_NAME, chatMessageDO.getClientId());
//        attachment.put(Constant.SERVANT_IDENTIFIER, chatMessageDO.getServantId());
//        attachment.put(Constant.TRAFFIC_MESSAGE_ID, chatMessageDO.getMessageId());
        attachment.put(Constant.TRAFFIC_MESSAGE_TYPE, chatMessageDO.getContentType());
        attachment.put(Constant.TRAFFIC_MESSAGE_FROM, chatMessageDO.getMessageFrom());
        attachment.put(Constant.TRAFFIC_MESSAGE_TO, chatMessageDO.getMessageTo());
        header.setAttachment(attachment);

        message.setHeader(header);
        message.setBody(chatMessageDO.getContent());
        return message;
    }

    public static ChatMessageDO nettyMessageToClientMessage(NettyMessage nettyMessage) {
        ChatMessageDO message = new ChatMessageDO();
        message.setMessageId(nettyMessage.getHeader().getMessageId());
        Map<String, Object> attachment = nettyMessage.getHeader().getAttachment();
        message.setMessageFrom((String) attachment.get(Constant.TRAFFIC_MESSAGE_FROM));
        message.setMessageTo((String) attachment.get(Constant.TRAFFIC_MESSAGE_TO));
        message.setContentType((Integer)attachment.get(Constant.TRAFFIC_MESSAGE_TYPE));
        message.setContent((String) nettyMessage.getBody());
        return message;
    }
}
