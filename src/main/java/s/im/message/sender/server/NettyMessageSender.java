package s.im.message.sender.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.message.server.NettyMessage;
import s.im.server.netty.api.IMNettyServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by za-zhujun on 2017/6/9.
 */
@Component
public class NettyMessageSender implements ServerMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyMessageSender.class);
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private IMNettyServer imNettyServer;

    @Override
    public void send(AddressInfo remoteAddress, NettyMessage nettyMessage) {
        executorService.submit(new NettyMessageSenderThread(remoteAddress, nettyMessage));
    }

    private class NettyMessageSenderThread implements Runnable {

        AddressInfo remoteAddress;
        NettyMessage nettyMessage;
        boolean success;
        int maxSendCount = 2;
        long intervalBeforeRetry = 1000L;
        AtomicInteger currentSendCount = new AtomicInteger(1);

        NettyMessageSenderThread(AddressInfo remoteAddress, NettyMessage nettyMessage) {
            this.remoteAddress = remoteAddress;
            this.nettyMessage = nettyMessage;
        }

        @Override
        public void run() {
            sendMessage();

            while(!success && currentSendCount.incrementAndGet() <= maxSendCount) {
                sendMessage();
                if (!success) {
                    // wait specified interval before re-persistAndSend
                    try {
                        TimeUnit.MILLISECONDS.sleep(intervalBeforeRetry);
                    } catch (InterruptedException e) {
                    }
                }
            }

            if (success) {
                //
            } else {
                // record error
            }
        }

        private void sendMessage() {
            ChannelFuture channelFuture = doSendNettyMessage();
            try {
                // future.get() not work. so sleep
                /*if (channelFuture != null) {
                    channelFuture.get();
                }*/
                Thread.sleep(2000);
            } catch (Exception e) {
                LOGGER.error("发送netty消息错误！！", e);
            }
        }

        private ChannelFuture doSendNettyMessage() {
            AddressInfo targetAddress = remoteAddress;
            LOGGER.info("查找连接到{} 的channel", targetAddress);
            Channel channel = imNettyServer.findInChannel(targetAddress);
            if (channel != null && channel.isActive()) {
                LOGGER.info("[{}/{}] 准备发送netty消息: {} -> {}, 消息ID：{}"
                        , currentSendCount.get()
                        , maxSendCount
                        , imNettyServer.getAddressInfo()
                        , targetAddress, nettyMessage.getHeader().getMessageId());
                ChannelFuture channelFuture = channel.writeAndFlush(nettyMessage);
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            LOGGER.error("发送错误", future.cause());
                            success = false;
                        } else {
                            LOGGER.info("发送成功！！！！！！");
                            success = true;
                        }
                    }
                });
                return channelFuture;
            } else {
                LOGGER.info("channel不存在：{} -> {}", imNettyServer.getAddressInfo(), targetAddress);
            }
            return null;
        }

    }
}
