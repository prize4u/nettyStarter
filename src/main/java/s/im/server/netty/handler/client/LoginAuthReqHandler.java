/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package s.im.server.netty.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.ServerState;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyClient;
import s.im.util.ChannelHandlerContextUtils;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthReqHandler.class);
    private IMNettyClient nettyClient;
//    private AddressInfo selfConnectAddressInfo;

    public LoginAuthReqHandler(IMNettyClient nettyClient) {
        this.nettyClient = nettyClient;
//        this.selfConnectAddressInfo = this.nettyClient.getSelfAddressInfo();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage loginMessage = NettyMessageFactory.newLoginReq();
        LOGGER.info("客户端发送login请求消息：{} --> {} with message {}", this.nettyClient.getAddressInfo(), this.nettyClient.getConnectingRemoteAddress(), loginMessage);
        ctx.writeAndFlush(loginMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手应答消息，需要判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            AddressInfo remoteAddressInfo = ChannelHandlerContextUtils.getAddressInfo(nettyClient.getAddressInfo(),
                    ctx);
            LOGGER.info("客户端接收登录返回消息 RESP {} --> {} with message {}", remoteAddressInfo, this.nettyClient
                    .getAddressInfo(), message);
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 1) {
                // 握手失败，关闭连接
                LOGGER.error("客户端登录失败 {} --> {}. 关闭通道", this.nettyClient.getAddressInfo(), remoteAddressInfo);
                ctx.close();
                nettyClient.setServerStatus(ServerState.Stopped);
//                nettyClient.setState(NettyServerConnectState.Disconnected);
            } else {
                LOGGER.info("客户端登录成功 {} --> {}", remoteAddressInfo, this.nettyClient.getAddressInfo());
                nettyClient.registOutChannel(remoteAddressInfo, ctx.channel());
//                nettyClient.recordOutcomeRemoteLogin(selfConnectAddressInfo, remoteAddressInfo, ctx.channel(), new Date());
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
