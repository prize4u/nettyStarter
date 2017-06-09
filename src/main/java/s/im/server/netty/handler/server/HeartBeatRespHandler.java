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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.server.netty.api.IMNettyServer;
import s.im.util.ChannelHandlerContextUtils;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatRespHandler.class);

    private final AddressInfo serverAddressInfo;
    private final IMNettyServer serverInstance;

    public HeartBeatRespHandler(IMNettyServer serverInstance) {
        this.serverAddressInfo = serverInstance.getAddressInfo();
        this.serverInstance = serverInstance;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        AddressInfo remoteAddress = ChannelHandlerContextUtils.getAddressInfo(ctx);
        // client channel closed
        ctx.close();
        serverInstance.registInChannel(remoteAddress, ctx.channel());
        LOGGER.info("服务端发现channel 失效 : {} --> {}", remoteAddress, this.serverInstance.getAddressInfo());
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 返回心跳应答消息
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            AddressInfo heartBeanFromAddressInfo = ChannelHandlerContextUtils.getAddressInfo(ctx);
//            LOGGER.info("服务器收到心跳请求 {} ---> {} with message {} ", heartBeanFromAddressInfo, serverAddressInfo, message);
            NettyMessage heartBeat = NettyMessageFactory.newHeartBeanResp();
//            LOGGER.info("服务器发送心跳响应 {} ---> {} with message {} ", serverAddressInfo, heartBeanFromAddressInfo, message);
            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
