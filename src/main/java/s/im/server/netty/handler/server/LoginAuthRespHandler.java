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
package s.im.server.netty.handler.server;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyServer;
import s.im.util.ChannelHandlerContextUtils;

import java.util.Date;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthRespHandler.class);

    private final AddressInfo serverServicingAddressInfo;
    private final IMNettyServer serverInstance;

    public LoginAuthRespHandler(IMNettyServer serverInstance) {
        this.serverInstance = serverInstance;
        this.serverServicingAddressInfo = serverInstance.getAddressInfo();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手请求消息，处理，其它消息透传
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            HostConnectionDetail connDetail = newConnectionDetail(ctx);
            LOGGER.info("服务端接受login请求消息 {} --> {} with message {}", connDetail.getSrcHost(), connDetail.getDestHost(), message);
            NettyMessage loginResp = null;
            // 重复登陆，拒绝
//                if (serverInstance.existIncomeConnection(connDetail.getSrcHost(), connDetail.getDestHost())) {
            if (serverInstance.existInConn(connDetail.getSrcHost())) {
                LOGGER.info("{} 已经在 {} 登录, 拒绝登录请求.", connDetail.getSrcHost(), connDetail.getDestHost());
                loginResp = NettyMessageFactory.newLoginResp((byte) -1);
            } else {
                // record login date
                boolean acceptedHost = serverInstance.canAcceptedHost(connDetail.getSrcHost().getIpAddress());
                if (acceptedHost) {
                    serverInstance.recordIn(connDetail.getSrcHost());
                    serverInstance.registInChannel(connDetail.getSrcHost(), ctx.channel());
                    LOGGER.info("登录成功 : {}", connDetail);
                }
                loginResp = acceptedHost ? NettyMessageFactory.newLoginResp((byte) 1) : NettyMessageFactory.newLoginResp((byte) -1);
            }
            LOGGER.info("发送登录返回消息 {} --> {} with message {}", this.serverServicingAddressInfo, connDetail.getSrcHost(), loginResp);
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("", cause);
        removeRemoteHostFromServerCache(ctx);
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private void removeRemoteHostFromServerCache(ChannelHandlerContext ctx) {
        AddressInfo remoteAddressInfo = ChannelHandlerContextUtils.getAddressInfo(serverInstance.getAddressInfo(), ctx);
        serverInstance.deregistInChannel(remoteAddressInfo, ctx.channel());
//        serverInstance.removeIncomeRemoteLogin(remoteAddressInfo, this.serverServicingAddressInfo, ctx.channel());
        LOGGER.info("remove host {} from connected remote ip, new connected remote ip: {} ", remoteAddressInfo, serverInstance.getIncomeRemoteHostDetail());
    }

    private HostConnectionDetail newConnectionDetail(ChannelHandlerContext ctx) {
        AddressInfo srcAddress = ChannelHandlerContextUtils.getAddressInfo(serverInstance.getAddressInfo(), ctx);
        return new HostConnectionDetail(srcAddress, serverServicingAddressInfo, new Date());
    }
}
