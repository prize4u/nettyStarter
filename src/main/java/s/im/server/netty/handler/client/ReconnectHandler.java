package s.im.server.netty.handler.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.server.netty.api.IMNettyClient;
import s.im.utils.Constant;

import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/4/21.
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReconnectHandler.class);

    private IMNettyClient nettyClient;

    public ReconnectHandler(IMNettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channel inactive : {} --> {}", this.nettyClient.getSelfAddressInfo(), this.nettyClient.getServerAddressInfo());
        LOGGER.info("schedule re-connect in {} seconds: {} --> {}", Constant.NETTY_CLIENT_RECONNECT_DELAY
                , this.nettyClient.getSelfAddressInfo(), this.nettyClient.getServerAddressInfo());
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                nettyClient.connect();
            }
        }, Constant.NETTY_CLIENT_RECONNECT_DELAY, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }
}
