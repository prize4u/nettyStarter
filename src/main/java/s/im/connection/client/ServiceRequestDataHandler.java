package s.im.connection.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.service.ChatMessagePersistService;
import s.im.service.route.api.MessageRouteService;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/6/9.
 */
@Component
public class ServiceRequestDataHandler implements ServerDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestDataHandler.class);

    @Autowired
    private MessageRouteService messageRouteService;
    @Autowired
    private ChatMessagePersistService messagePersistService;

    @Override
    public void onMessage(AddressInfo routeFromAddress, NettyMessage nettyMessage) {
        if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.SERVICE_REQ.value()) {
            LOGGER.info("收到来自 {} 的请求消息， 消息：{}", routeFromAddress, nettyMessage);
            ChatMessageDO chatMessageDO = NettyMessageFactory.nettyMessageToClientMessage(nettyMessage);
            messagePersistService.updateRouteDestReceiveTime(chatMessageDO.getMessageId(), new Date());
            messageRouteService.route(chatMessageDO);
        }
    }
}
