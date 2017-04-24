package s.im.server.netty.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.entity.NettyServerConnectState;
import s.im.server.netty.api.IMNettyClient;
import s.im.server.netty.api.IMNettyServer;
import s.im.server.netty.api.NettyOperationCallback;
import s.im.server.netty.codec.NettyMessageDecoder;
import s.im.server.netty.codec.NettyMessageEncoder;
import s.im.server.netty.handler.client.ClientChannelConnectionHandler;
import s.im.server.netty.handler.client.LoginAuthReqHandler;
import s.im.utils.ChannelUtils;
import s.im.utils.Constant;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class IMNettyClientImpl implements IMNettyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMNettyClientImpl.class);

    private final AddressInfo selfAddressInfo;
    private final AddressInfo serverAddressInfo;
    private final IMNettyServer nettyServer;
    private EventLoopGroup eventLoop;
    private NettyServerConnectState nettyServerConnectState = NettyServerConnectState.Disconnected;
    private Channel channel;

    public IMNettyClientImpl(IMNettyServer nettyServer, AddressInfo selfAddressInfo, AddressInfo serverAddressInfo) {
        this.nettyServer = nettyServer;
        this.selfAddressInfo = selfAddressInfo;
        this.serverAddressInfo = serverAddressInfo;
        eventLoop = new NioEventLoopGroup();
    }

    @Override
    public void connect() {
        if (!isConnected()) {
            final IMNettyClient clientInstanct = this;
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoop)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                    ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                    ch.pipeline().addLast(new IdleStateHandler(Constant.CLIENT_READ_IDEL_TIME_OUT,
                            Constant.CLIENT_WRITE_IDEL_TIME_OUT, Constant.CLIENT_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
                    ch.pipeline().addLast("connectHandler", new ClientChannelConnectionHandler(IMNettyClientImpl.this));
//                    ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(Constant.NETTY_TIMEOUT_IN_SECONDS));
                    ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler(clientInstanct));
//                    ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler(clientInstanct));
                }
            });

            setState(NettyServerConnectState.Connecting);
            LOGGER.info("客户端尝试建立channel : {} --> {}", selfAddressInfo, serverAddressInfo);
            ChannelFuture channelFuture = null;
            try {
                channelFuture = bootstrap.connect(new InetSocketAddress(serverAddressInfo.getIpAddress(), serverAddressInfo.getPort())
                        , new InetSocketAddress(selfAddressInfo.getIpAddress(), selfAddressInfo.getPort())).sync();
                if (!channelFuture.isSuccess()) {
                    LOGGER.error("客户端建立channel失败 : {} --> {}", selfAddressInfo, serverAddressInfo, channelFuture.cause());
                    // client connect to server failed,reconnect
                    setState(NettyServerConnectState.Disconnected);
                    reconnect();
                } else {
                    LOGGER.info("客户端建立channel成功 : {} --> {}", selfAddressInfo, serverAddressInfo);
                    setState(NettyServerConnectState.Connected);
                    channel = channelFuture.channel();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void disconnect() {
        if (this.channel != null) {
            LOGGER.info("client channel closing : {} --> {}", selfAddressInfo, serverAddressInfo);
            ChannelFuture channelFuture = this.channel.close().syncUninterruptibly();
            if (channelFuture.isSuccess()) {
                LOGGER.info("close client channel ok : {} --> {}", selfAddressInfo, serverAddressInfo);
            } else {
                setState(NettyServerConnectState.Disconnected);
            }
        }
    }

    @Override
    public void disconnectAsync(final NettyOperationCallback callback) {
        if (this.channel != null) {
            ChannelFuture channelFuture = this.channel.close();
            if (callback != null) {
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailed();
                        }
                    }
                });
            }
        }
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void reconnect() {
        if (isConnected()) {
            disconnectAsync(new NettyOperationCallback() {
                @Override
                public void onSuccess() {
                    setState(NettyServerConnectState.Disconnected);
                    doReconnect();
                }

                @Override
                public void onFailed() {
                    LOGGER.info("第{}次重连失败，尝试再次重连: {} --> {}", getSelfAddressInfo(), getServerAddressInfo());
                    doReconnect();
                }
            });
        } else {
            doReconnect();
        }
    }

    private void doReconnect() {
        LOGGER.info("{} 秒后计划重连: {} --> {}", Constant.NETTY_CLIENT_RECONNECT_DELAY
                , getSelfAddressInfo(), getServerAddressInfo());
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }, Constant.NETTY_CLIENT_RECONNECT_DELAY, TimeUnit.SECONDS);
    }

    @Override
    public void shutdown() {
        if (isConnected()) {
            eventLoop.shutdownGracefully();
            eventLoop = null; // help GC
        }
    }

    @Override
    public AddressInfo getSelfAddressInfo() {
        return selfAddressInfo;
    }

    @Override
    public AddressInfo getServerAddressInfo() {
        return serverAddressInfo;
    }

    @Override
    public boolean isConnected() {
        return ChannelUtils.isOpenAndActive(this.channel);
    }

    @Override
    public void setState(NettyServerConnectState nettyServerConnectState) {
        this.nettyServerConnectState = nettyServerConnectState;
    }

    @Override
    public NettyServerConnectState getState() {
        return nettyServerConnectState;
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return nettyServer.getOutcomeRemoteHostDetail();
    }

    @Override
    public void reocrdOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel, Date connDate) {
        nettyServer.reocrdOutcomeRemoteLogin(srcHost, destHost, channel, connDate);
    }

    @Override
    public void removeOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel) {
        nettyServer.removeOutcomeRemoteLogin(srcHost, destHost, channel);
    }

    @Override
    public boolean existOutcomeConnection(AddressInfo srcHost, AddressInfo destHost) {
        return nettyServer.existOutcomeConnection(srcHost, destHost);
    }

    @Override
    public void reocrdOutcomeRemoteLogin(HostConnectionDetail conn) {
        nettyServer.reocrdOutcomeRemoteLogin(conn);
    }


}


