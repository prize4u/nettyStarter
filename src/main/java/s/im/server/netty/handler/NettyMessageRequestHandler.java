package s.im.server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyClient;
import s.im.util.ChannelHandlerContextUtils;

public class NettyMessageRequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMessageRequestHandler.class);
    private AddressInfo addressInfo;
    private ServerDataHandler serverDataHandler;

    public NettyMessageRequestHandler(AddressInfo addressInfo, ServerDataHandler serverDataHandler) {
        this.addressInfo = addressInfo;
        this.serverDataHandler = serverDataHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if (message.getHeader() != null && message.getHeader().getType() == MessageType.SERVICE_REQ.value()) {
            AddressInfo remoteAddress = ChannelHandlerContextUtils.getAddressInfo(ctx);
            String messageId = message.getHeader().getMessageId();
            LOGGER.info("{} 收到来自 {} 的请求消息， 消息：{}", addressInfo, remoteAddress, message);

            // send back ack message
            NettyMessage nettyMessage = NettyMessageFactory.newServiceResp(messageId);
            ctx.writeAndFlush(nettyMessage);

            // process
            serverDataHandler.onMessage(remoteAddress, message);
        } else {
            ctx.fireChannelRead(msg);
        }

        /**/
    }
}
