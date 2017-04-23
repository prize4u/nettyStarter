package s.im.server.netty.handler.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.server.netty.api.IMNettyServer;
import s.im.utils.ChannelHandlerContextUtils;

/**
 * Created by za-zhujun on 2017/4/21.
 */
public class ClientConnectionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnectionHandler.class);

    private IMNettyServer serverInstance;
    public ClientConnectionHandler(IMNettyServer serverInstance) {
        this.serverInstance = serverInstance;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        AddressInfo remoteAddress = ChannelHandlerContextUtils.getAddressInfo(ctx);
        // client channel closed
        ctx.close();
        LOGGER.info("channel inactive : {} --> {}", remoteAddress, this.serverInstance.getAddressInfo());
        super.channelInactive(ctx);
//        LOGGER.info("channel inactive : {} --> {}", this.nettyClient.getSelfAddressInfo(), this.nettyClient.getServerAddressInfo());
//        LOGGER.info("schedule re-connect in {} seconds: {} --> {}", Constant.NETTY_CLIENT_RECONNECT_DELAY
//                , this.nettyClient.getSelfAddressInfo(), this.nettyClient.getServerAddressInfo());
//        final EventLoop eventLoop = ctx.channel().eventLoop();
//        eventLoop.schedule(new Runnable() {
//            @Override
//            public void run() {
//                nettyClient.connect();
//            }
//        }, Constant.NETTY_CLIENT_RECONNECT_DELAY, TimeUnit.SECONDS);
//        super.channelInactive(ctx);
    }
}
