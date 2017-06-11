package s.im.server.netty.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyClient;
import s.im.util.ChannelHandlerContextUtils;
import s.im.util.Constant;

public class ClientChannelConnectionHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientChannelConnectionHandler.class);
	private int loss_connect_time = 0;
	private int noWriteTimeInSeconds = 0;
	private final IMNettyClient imNettyClient;

	public ClientChannelConnectionHandler(IMNettyClient imNettyClient) {
		this.imNettyClient = imNettyClient;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		AddressInfo remoteAddressInfo = ChannelHandlerContextUtils.getAddressInfo(ctx);
		LOGGER.info("客服端channel失效 : {} --> {}", this.imNettyClient.getAddressInfo(), remoteAddressInfo);
//		imNettyClient.removeOutcomeRemoteLogin(this.imNettyClient.getAddressInfo(), remoteAddressInfo, ctx.channel());
		imNettyClient.deregistOutChannel(remoteAddressInfo, ctx.channel());
		imNettyClient.reconnect();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.WRITER_IDLE) {
				noWriteTimeInSeconds += Constant.CLIENT_WRITE_IDEL_TIME_OUT;
				loss_connect_time++;
				LOGGER.info("{}秒没有向服务器 {} --> {} 写信息了", noWriteTimeInSeconds, this.imNettyClient.getAddressInfo(), this.imNettyClient.getConnectingRemoteAddress());
				if (loss_connect_time > 1) {
					//
					LOGGER.info("链接丢失 {} --> {}", noWriteTimeInSeconds, this.imNettyClient.getAddressInfo(), this.imNettyClient.getConnectingRemoteAddress());
//					imNettyClient.removeOutcomeRemoteLogin(this.imNettyClient.getAddressInfo(), this.imNettyClient.getConnectingRemoteAddress(), ctx.channel());
					imNettyClient.deregistOutChannel(this.imNettyClient.getConnectingRemoteAddress(), ctx.channel());
					imNettyClient.reconnect();
				} else {
					// persistAndSend heart bean message
					NettyMessage heatBeat = NettyMessageFactory.newHeartBeanReq();
					LOGGER.info("发送心跳请求 {} ---> {} with message {} ", this.imNettyClient.getAddressInfo(), this.imNettyClient.getConnectingRemoteAddress(), heatBeat);
					ctx.writeAndFlush(heatBeat);
					noWriteTimeInSeconds = 0;
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		loss_connect_time--;

		// 心跳响应
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
			LOGGER.info("收到心跳响应 {} ---> {} with message {} ", this.imNettyClient.getAddressInfo(), this.imNettyClient.getConnectingRemoteAddress());
		}
		ctx.fireChannelRead(msg);
	}
}
