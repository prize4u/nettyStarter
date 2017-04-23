package s.im.server.netty.impl;

import com.google.common.collect.Lists;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.entity.NettyServerState;
import s.im.exception.NettyServerException;
import s.im.server.netty.api.IMNettyClient;
import s.im.server.netty.api.IMNettyServer;
import s.im.server.netty.api.NettyOperationCallback;
import s.im.server.netty.codec.NettyMessageDecoder;
import s.im.server.netty.codec.NettyMessageEncoder;
import s.im.server.netty.handler.server.HostConnectionRecorder;
import s.im.server.netty.handler.server.HostConnectionRecorderImpl;
import s.im.server.netty.handler.server.ServerChannelConnectionHandler;
import s.im.utils.Constant;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class IMNettyServerImpl implements IMNettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMNettyServerImpl.class);

    private final AddressInfo addressInfo;
    private final Set<String> whiteList;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private NettyServerState serverState = NettyServerState.Stopped;
    private HostConnectionRecorder connectionRecorder = new HostConnectionRecorderImpl();
    private ConcurrentHashMap<String, IMNettyClient> nettyClientMap = new ConcurrentHashMap<>();

    public IMNettyServerImpl(AddressInfo addressInfo, Set<String> whiteList) {
        this.addressInfo = addressInfo;
        this.whiteList = whiteList;
    }

    public void setServerState(NettyServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public boolean isAcceptedHost(String hostIP) {
        return whiteList.contains(hostIP);
    }

    @Override
    public void start() throws NettyServerException {
        if (isRunning() || isStarting()) {
            LOGGER.error("netty服务端已经启动 " + addressInfo);
            throw new NettyServerException("netty server already running at " + addressInfo);
        }

        initServerAttribute();

        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.bind(addressInfo.getIpAddress(), addressInfo.getPort()).sync();
            if (channelFuture.isSuccess()) {
                Channel channel = channelFuture.channel();
                boolean channelOpenAndActive = channel != null && channel.isOpen() && channel.isActive();
                setServerState(channelOpenAndActive ? NettyServerState.Running : NettyServerState.Stopped);
                if (channelOpenAndActive) {
                    LOGGER.info("Netty服务器启动成功 {}", addressInfo);
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
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws IOException {
                ch.pipeline().addLast(new IdleStateHandler(Constant.SERVER_READ_IDEL_TIME_OUT,
                        Constant.SERVER_WRITE_IDEL_TIME_OUT, Constant.SERVER_ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
                ch.pipeline().addLast(new ServerChannelConnectionHandler(IMNettyServerImpl.this));
                ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                ch.pipeline().addLast(new NettyMessageEncoder());

//                ch.pipeline().addLast(new ClientConnectionHandler(serverInstance));
//                ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(Constant.NETTY_TIMEOUT_IN_SECONDS));
//                ch.pipeline().addLast(new LoginAuthRespHandler(serverInstance));
//                ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler(serverInstance));
            }
        });
        setServerState(NettyServerState.Starting);
    }

    @Override
    public void startAsyn(final NettyOperationCallback callback) {
        /*if (isRunning() || isStarting()) {
            throw new NettyServerException("netty server already running at " + addressInfo);
        }*/

        initServerAttribute();

        ChannelFuture future = bootstrap.bind(addressInfo.getIpAddress(), addressInfo.getPort()).syncUninterruptibly();
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    setServerState(NettyServerState.Running);
                    callback.onSuccess();
                } else {
                    callback.onFailed();
                }
            }
        });
    }

    @Override
    public void stop() {
        if (isRunning() || isStarting()) {
            setServerState(NettyServerState.Stopping);
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            workerGroup = null; // help GC
            bossGroup = null; // help GC
            setServerState(NettyServerState.Stopped);
        }
    }

    @Override
    public void restart() {
        setServerState(NettyServerState.Restarting);
        stop();
    }

    @Override
    public AddressInfo getAddressInfo() {
        return this.addressInfo;
    }

    @Override
    public boolean isRunning() {
        return workerGroup != null && bossGroup != null && NettyServerState.Running.equals(this.serverState);
    }

    private boolean isStarting() {
        return workerGroup != null && bossGroup != null && (NettyServerState.Restarting.equals(this.serverState) || NettyServerState.Starting.equals(this.serverState));
    }

    @Override
    public NettyServerState getState() {
        return this.serverState;
    }

    @Override
    public void setState(NettyServerState nettyServerState) {
        this.serverState = nettyServerState;
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return connectionRecorder.getIncomeRemoteHostDetail();
    }

    @Override
    public void reocrdIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Date loginDate) {
        connectionRecorder.reocrdIncomeRemoteLogin(srcHost, destHost, loginDate);
    }

    @Override
    public void removeIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost) {
        connectionRecorder.removeIncomeRemoteLogin(srcHost, destHost);
    }

    @Override
    public boolean existIncomeConnection(AddressInfo srcHost, AddressInfo destHost) {
        return connectionRecorder.existIncomeConnection(srcHost, destHost);
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return connectionRecorder.getOutcomeRemoteHostDetail();
    }

    @Override
    public void reocrdOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Date connDate) {
        connectionRecorder.reocrdOutcomeRemoteLogin(srcHost, destHost, connDate);
    }

    @Override
    public void removeOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost) {
        connectionRecorder.removeOutcomeRemoteLogin(srcHost, destHost);
    }

    @Override
    public boolean existOutcomeConnection(AddressInfo srcHost, AddressInfo destHost) {
        return connectionRecorder.existOutcomeConnection(srcHost, destHost);
    }

    @Override
    public void reocrdIncomeRemoteLogin(HostConnectionDetail conn) {
        connectionRecorder.reocrdIncomeRemoteLogin(conn);
    }

    @Override
    public void reocrdOutcomeRemoteLogin(HostConnectionDetail conn) {
        connectionRecorder.reocrdOutcomeRemoteLogin(conn);
    }

    @Override
    public boolean addNettyClient(IMNettyClient newNettyClient) {
        String clientKey = buildClientKey(newNettyClient.getSelfAddressInfo(), newNettyClient.getServerAddressInfo());
        nettyClientMap.put(clientKey, newNettyClient);
        return true;
    }

    @Override
    public List<IMNettyClient> getAllNettyClient() {
        return Lists.newArrayList(nettyClientMap.values());
    }

    @Override
    public IMNettyClient getNettyClient(AddressInfo srcHost, AddressInfo destHost) {
        String clientKey = buildClientKey(srcHost, destHost);
        return nettyClientMap.get(clientKey);
    }

    private String buildClientKey(AddressInfo srcHost, AddressInfo destHost) {
        return srcHost + "-->" + destHost;
    }
}
