package s.im.server.netty.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyServer;
import s.im.util.ChannelHandlerContextUtils;

public class NettyMessageRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMessageRespHandler.class);

    private final AddressInfo serverServicingAddressInfo;
    private final IMNettyServer serverInstance;
    private final ServerDataHandler nettyMessageHandler;

    public NettyMessageRespHandler(IMNettyServer serverInstance, ServerDataHandler nettyMessageHandler) {
        this.serverInstance = serverInstance;
        this.serverServicingAddressInfo = serverInstance.getAddressInfo();
        this.nettyMessageHandler = nettyMessageHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是业务请求消息，处理，其它消息透传
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.SERVICE_REQ.value()) {
            AddressInfo remoteAddress = ChannelHandlerContextUtils.getAddressInfo(ctx);
            String messageId = message.getHeader().getMessageId();
            LOGGER.info("{} 收到来自 {} 的请求消息， 消息ID：{}", serverServicingAddressInfo, remoteAddress, messageId);

            // persistAndSend back ack message
            NettyMessage nettyMessage = NettyMessageFactory.newServiceResp(messageId);
            ctx.writeAndFlush(nettyMessage);

            nettyMessageHandler.onMessage(remoteAddress, nettyMessage);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("", cause);
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
