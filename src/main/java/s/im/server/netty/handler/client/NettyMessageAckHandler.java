package s.im.server.netty.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.connection.client.ServerDataHandlerFactory;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyClient;
import s.im.service.ChatMessagePersistService;
import s.im.util.ChannelHandlerContextUtils;

import java.util.Date;

public class NettyMessageAckHandler extends ChannelInboundHandlerAdapter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMessageAckHandler.class);
//    private IMNettyClient nettyClient;
//    private ChatMessagePersistService clienChatMessagePersistService;

//    public NettyMessageAckHandler(IMNettyClient nettyClient, ChatMessagePersistService clienChatMessagePersistService) {
//        this.nettyClient = nettyClient;
//        this.clienChatMessagePersistService = clienChatMessagePersistService;
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if (ServerDataHandlerFactory.isValidMessageType(message.getHeader().getType())) {
            AddressInfo remoteAddress = ChannelHandlerContextUtils.getAddressInfo(ctx);
            ServerDataHandler handler = ServerDataHandlerFactory.getHandler(MessageType.getMessageType(message.getHeader().getType()));
            handler.onMessage(remoteAddress, message);
        } else {
            ctx.fireChannelRead(msg);
        }

//        if (message.getHeader() != null && message.getHeader().getType() == MessageType.SERVICE_REQ.value()) {
//            AddressInfo remoteAddress = ChannelHandlerContextUtils.getAddressInfo(ctx);
//            String messageId = message.getHeader().getMessageId();
//            LOGGER.info("{} 收到来自 {} 的请求消息， 消息：{}", nettyClient.getAddressInfo(), remoteAddress, message);
//
//            // persistAndSend back ack message
//            NettyMessage nettyMessage = NettyMessageFactory.newServiceResp(messageId);
//            ctx.writeAndFlush(nettyMessage);
//
//            nettyMessageHandler.onMessage(remoteAddress, nettyMessage);
//        } else if (message.getHeader() != null && message.getHeader().getType() == MessageType.SERVICE_RESP.value()) {
//            AddressInfo remoteAddressInfo = ChannelHandlerContextUtils.getAddressInfo(ctx);
//            String ackMessageId = (String) message.getBody();
//            LOGGER.info("{} 收到来自 {} 的确认消息，消息ID {}"
//                    , this.nettyClient.getAddressInfo()
//                    , remoteAddressInfo
//                    , ackMessageId);
//
//            // update
//            clienChatMessagePersistService.updateRouteAckTime(ackMessageId, new Date());
//        } else {
//            ctx.fireChannelRead(msg);
//        }
    }
}
