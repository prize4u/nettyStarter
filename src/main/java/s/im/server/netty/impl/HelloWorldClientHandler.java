package s.im.server.netty.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import s.im.message.server.NettyMessage;

public class HelloWorldClientHandler extends ChannelInboundHandlerAdapter{
    
    
      @Override
      public void channelActive(ChannelHandlerContext ctx) {
          System.out.println("HelloWorldClientHandler Active");
          ctx.fireChannelActive();
      }
  
      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) {
          if (msg instanceof NettyMessage) {
              System.out.println("HelloWorldClientHandler read Message:"+msg);
          }
      }
      
      @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
          System.out.println("HelloWorldClientHandler inActive===========");
        super.channelInactive(ctx);
    }
  
  
     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
         ctx.close();
      }

}
