package s.im.server.netty.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.server.netty.api.IMNettyServer;
import s.im.utils.ChannelHandlerContextUtils;
import s.im.utils.Constant;

public class ServerChannelConnectionHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerChannelConnectionHandler.class);
	private int loss_connect_time = 0;
	private int noReadTime = 0;
	private final IMNettyServer imNettyServer;

	public ServerChannelConnectionHandler(IMNettyServer imNettyServer) {
		this.imNettyServer = imNettyServer;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				noReadTime += Constant.SERVER_READ_IDEL_TIME_OUT;
				loss_connect_time++;
				LOGGER.info("{}秒没有接收到客户端的信息了", noReadTime);
				if (loss_connect_time >= 2) {
					AddressInfo remoteAddressInfo = ChannelHandlerContextUtils.getAddressInfo(ctx);
					LOGGER.info("服务端关闭Channel：{} -->{}", this.imNettyServer.getAddressInfo(), remoteAddressInfo);
					this.imNettyServer.removeIncomeRemoteLogin(remoteAddressInfo, this.imNettyServer.getAddressInfo(), ctx.channel());
					ctx.channel().close();
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}
