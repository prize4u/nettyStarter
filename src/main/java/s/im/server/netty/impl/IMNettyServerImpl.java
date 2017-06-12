package s.im.server.netty.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.entity.ServerState;
import s.im.server.netty.api.AbstractIMNettyServer;
import s.im.server.netty.api.IMNettyClient;
import s.im.server.netty.api.IMNettyServer;
import s.im.server.netty.codec.NettyMessageDecoder;
import s.im.server.netty.codec.NettyMessageEncoder;
import s.im.server.netty.handler.server.*;
import s.im.util.Constant;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class IMNettyServerImpl extends AbstractIMNettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMNettyServerImpl.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private ServerDataHandler nettyMessageHandler;
    private ConcurrentHashMap<String, IMNettyClient> nettyClientMap = new ConcurrentHashMap<>();

    public IMNettyServerImpl(AddressInfo addressInfo) {
        super(addressInfo);
    }


    @Override
    protected void doStartServer() {
        initServerAttribute();

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.bind(getAddressInfo().getIpAddress(), getAddressInfo().getPort()).sync();
            if (channelFuture.isSuccess()) {
                Channel channel = channelFuture.channel();
                boolean channelOpenAndActive = channel != null && channel.isOpen() && channel.isActive();
                setServerStatus(channelOpenAndActive ? ServerState.Running : ServerState.Stopped);
                if (channelOpenAndActive) {
                    LOGGER.info("Netty服务器启动成功 {}", getAddressInfo());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initServerAttribute() {
        // do start netty server
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        final IMNettyServer serverInstance = this;
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws IOException {
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast(new HelloWorldServerHandler(IMNettyServerImpl.this));
//                    ch.pipeline().addLast(new IdleStateHandler(Constant.SERVER_READ_IDEL_TIME_OUT, Constant.SERVER_WRITE_IDEL_TIME_OUT, Constant.SERVER_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
//                    ch.pipeline().addLast(new ServerChannelConnectionHandler(IMNettyServerImpl.this));
//                    ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
//                    ch.pipeline().addLast(new NettyMessageEncoder());
//
//                    ch.pipeline().addLast(new LoginAuthRespHandler(IMNettyServerImpl.this));
//                    ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler(IMNettyServerImpl.this));
//                    ch.pipeline().addLast("ServiceRespHandler", new NettyMessageRespHandler(IMNettyServerImpl.this,
//                            nettyMessageHandler));

            }
        });
        setServerStatus(ServerState.Starting);
    }

    @Override
    protected void doStopServer() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        workerGroup = null; // help GC
        bossGroup = null; // help GC
    }

    @Override
    public boolean isRunning() {
        return workerGroup != null && bossGroup != null && ServerState.Running.equals(this.serverState);
    }

    @Override
    public boolean addNettyClient(IMNettyClient newNettyClient) {
        String clientKey = buildClientKey(newNettyClient.getAddressInfo(), newNettyClient.getConnectingRemoteAddress());
        nettyClientMap.put(clientKey, newNettyClient);
        return true;
    }


//    @Override
//    public List<IMNettyClient> getAllNettyClient() {
//        return Lists.newArrayList(nettyClientMap.values());
//    }

//    @Override
//    public IMNettyClient getNettyClient(AddressInfo srcHost, AddressInfo destHost) {
//        String clientKey = buildClientKey(srcHost, destHost);
//        return nettyClientMap.get(clientKey);
//    }


    public void setNettyMessageHandler(ServerDataHandler nettyMessageHandler) {
        this.nettyMessageHandler = nettyMessageHandler;
    }

    private String buildClientKey(AddressInfo srcHost, AddressInfo destHost) {
        return srcHost + "-->" + destHost;
    }
}
