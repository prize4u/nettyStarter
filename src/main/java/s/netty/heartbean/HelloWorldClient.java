package s.netty.heartbean;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloWorldClient {

    public void connect(int port, String host) throws Exception {
        ChannelFuture future = null;
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap().group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("decoder", new StringDecoder());
                p.addLast("encoder", new StringEncoder());
//                        p.addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
//                        p.addLast(new BaseClientHandler());
            }
        });

        try {
            future = b.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush("Hello Netty Server ,I am a common client");
            Thread.sleep(5000);// sleep 5 s
            ChannelFuture closeFuture = channel.close();
            closeFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    System.out.println(channelFuture);
                }
            });
//            future.channel().closeFuture().sync();
        } finally {
            // group.shutdownGracefully();
            if (null != future) {
                if (future.channel() != null && future.channel().isOpen()) {
                    future.channel().close();
                }
            }
            System.out.println("准备重连");
            connect(port, host);
            System.out.println("重连成功");
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new HelloWorldClient().connect(port, "192.168.0.104");
    }

}
