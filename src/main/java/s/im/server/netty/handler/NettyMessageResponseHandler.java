package s.im.server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.server.netty.api.IMNettyClient;
import s.im.service.ChatMessagePersistService;
import s.im.util.ChannelHandlerContextUtils;

import java.util.Date;

public class NettyMessageResponseHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMessageResponseHandler.class);
    private IMNettyClient nettyClient;
    private ChatMessagePersistService clienChatMessagePersistService;

    public NettyMessageResponseHandler(IMNettyClient nettyClient, ChatMessagePersistService clienChatMessagePersistService) {
        this.nettyClient = nettyClient;
        this.clienChatMessagePersistService = clienChatMessagePersistService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if (message.getHeader() != null && message.getHeader().getType() == MessageType.SERVICE_RESP.value()) {
            AddressInfo remoteAddressInfo = ChannelHandlerContextUtils.getAddressInfo(ctx);
            String ackMessageId = (String) message.getBody();
            LOGGER.info("{} 收到来自 {} 的确认消息，消息ID {}"
                    , this.nettyClient.getAddressInfo()
                    , remoteAddressInfo
                    , ackMessageId);

            // update
            clienChatMessagePersistService.updateRouteAckTime(ackMessageId, new Date());
        } else {
            ctx.fireChannelRead(msg);
        }

    }
}
