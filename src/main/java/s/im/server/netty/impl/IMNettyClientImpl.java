package s.im.server.netty.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.message.server.NettyMessage;
import s.im.server.netty.api.AbstractIMNettyClient;
import s.im.server.netty.api.IMNettyServer;
import s.im.server.netty.handler.client.ClientChannelConnectionHandler;
import s.im.server.netty.handler.client.LoginAuthReqHandler;
import s.im.server.netty.handler.NettyMessageRequestHandler;
import s.im.service.ChatMessagePersistService;
import s.im.util.Constant;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class IMNettyClientImpl extends AbstractIMNettyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMNettyClientImpl.class);
    private ChatMessagePersistService clienChatMessagePersistService;
    private ServerDataHandler serverDataHandler;
    private EventLoopGroup eventLoop;
    private Channel channel;

    public IMNettyClientImpl(AddressInfo addressInfo, AddressInfo remoteAddressInfo, IMNettyServer nestNettyServer) {
        super(addressInfo, remoteAddressInfo, nestNettyServer);
        eventLoop = new NioEventLoopGroup();
    }

    @Override
    protected void doConnect() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoop)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
//                        ChannelPipeline p = ch.pipeline();
//                        p.addLast("decoder", new s.im.server.netty.codec.jackson.NettyMessageEncoder());
//                        p.addLast("encoder", new s.im.server.netty.codec.jackson.NettyMessageDecoder<>(NettyMessage.class));
//                        p.addLast(new HelloWorldClientHandler());

//                        ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
//                        ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
//                        ch.pipeline().addLast(new IdleStateHandler(Constant.CLIENT_READ_IDEL_TIME_OUT,
//                                Constant.CLIENT_WRITE_IDEL_TIME_OUT, Constant.CLIENT_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
//                        ch.pipeline().addLast("connectHandler", new ClientChannelConnectionHandler(IMNettyClientImpl.this));
//                        ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler(IMNettyClientImpl.this));
//                        ch.pipeline().addLast("ServiceMessageAckHandler", new NettyMessageRequestHandler
//                                (IMNettyClientImpl.this, clienChatMessagePersistService));

                        ch.pipeline().addLast(new s.im.server.netty.codec.jackson.NettyMessageEncoder());
                        ch.pipeline().addLast(new s.im.server.netty.codec.jackson.NettyMessageDecoder<>(NettyMessage.class));
                        ch.pipeline().addLast(new IdleStateHandler(Constant.CLIENT_READ_IDEL_TIME_OUT,
                                Constant.CLIENT_WRITE_IDEL_TIME_OUT, Constant.CLIENT_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
                        ch.pipeline().addLast("connectHandler", new ClientChannelConnectionHandler(IMNettyClientImpl.this));
                        ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler(IMNettyClientImpl.this));
                        ch.pipeline().addLast("ServiceMessageRequestHandler", new NettyMessageRequestHandler
                                (getAddressInfo(), serverDataHandler));
                    }
                });

//        setState(NettyServerConnectState.Connecting);
        LOGGER.info("netty客户端尝试建立channel : {} --> {}", getAddressInfo(), getConnectingRemoteAddress());
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(
                    new InetSocketAddress(getConnectingRemoteAddress().getIpAddress(), getConnectingRemoteAddress().getPort())
                    , new InetSocketAddress(getAddressInfo().getIpAddress(), getAddressInfo().getPort())).sync();
            if (!channelFuture.isSuccess()) {
                LOGGER.error("netty客户端建立channel失败 : {} --> {}", getAddressInfo(), getConnectingRemoteAddress(), channelFuture.cause());
                // client connect to server failed,reconnect
//                setState(NettyServerConnectState.Disconnected);
//                reconnect();
            } else {
                LOGGER.info("netty客户端建立channel成功 : {} --> {}", getAddressInfo(), getConnectingRemoteAddress());
//                setState(NettyServerConnectState.Connected);
                channel = channelFuture.channel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDisConnect() {
        if (this.channel != null) {
            LOGGER.info("netty客户端尝试关闭channel : {} --> {}", getAddressInfo(), getConnectingRemoteAddress());
            ChannelFuture channelFuture = this.channel.close().syncUninterruptibly();
            if (channelFuture.isSuccess()) {
                LOGGER.info("netty客户端尝试关闭channel : {} --> {} 成功", getAddressInfo(), getConnectingRemoteAddress());
            } else {
//                setState(NettyServerConnectState.Disconnected);
            }
        }
    }


//    @Override
//    public void disconnectAsync(final NettyOperationCallback callback) {
//        if (this.channel != null) {
//            ChannelFuture channelFuture = this.channel.close();
//            if (callback != null) {
//                channelFuture.addListener(new ChannelFutureListener() {
//                    @Override
//                    public void operationComplete(ChannelFuture future) throws Exception {
//                        if (future.isSuccess()) {
//                            callback.onSuccess();
//                        } else {
//                            callback.onFailed();
//                        }
//                    }
//                });
//            }
//        }
//    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

//    @Override
//    public void reconnect() {
//        if (isConnected()) {
//            disconnectAsync(new NettyOperationCallback() {
//                @Override
//                public void onSuccess() {
//                    setState(NettyServerConnectState.Disconnected);
//                    doReconnect();
//                }
//
//                @Override
//                public void onFailed() {
//                    LOGGER.info("第{}次重连失败，尝试再次重连: {} --> {}", getSelfAddressInfo(), getServerAddressInfo());
//                    doReconnect();
//                }
//            });
//        } else {
//            doReconnect();
//        }
//    }

//    private void doReconnect() {
//        LOGGER.info("{} 秒后计划重连: {} --> {}", Constant.NETTY_CLIENT_RECONNECT_DELAY
//                , getSelfAddressInfo(), getServerAddressInfo());
//        eventLoop.schedule(new Runnable() {
//            @Override
//            public void run() {
//                connect();
//            }
//        }, Constant.NETTY_CLIENT_RECONNECT_DELAY, TimeUnit.SECONDS);
//    }


    public void setClienChatMessagePersistService(ChatMessagePersistService clienChatMessagePersistService) {
        this.clienChatMessagePersistService = clienChatMessagePersistService;
    }

    public void setServerDataHandler(ServerDataHandler serverDataHandler) {
        this.serverDataHandler = serverDataHandler;
    }

    @Override
    public void shutdown() {
        if (isRunning()) {
            eventLoop.shutdownGracefully();
            eventLoop = null; // help GC
        }
    }

}


