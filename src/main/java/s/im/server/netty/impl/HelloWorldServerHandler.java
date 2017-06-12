package s.im.server.netty.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import s.im.server.netty.api.IMNettyServer;
import s.im.util.ChannelHandlerContextUtils;
import s.netty.heartbean.HelloWorldServer;

public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter{
    
	private final IMNettyServer server;
	
	public HelloWorldServerHandler(IMNettyServer server) {
		this.server = server;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		server.registInChannel(ChannelHandlerContextUtils.getAddressInfo(ctx), ctx.channel());
		super.channelActive(ctx);
	}
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead..");
        System.out.println(ctx.channel().remoteAddress()+"->Server :"+ msg.toString());
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
