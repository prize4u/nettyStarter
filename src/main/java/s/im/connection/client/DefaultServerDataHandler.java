package s.im.connection.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.service.ChatMessagePersistService;
import s.im.service.route.api.MessageRouteService;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/6/9.
 */
@Component
public class DefaultServerDataHandler implements ServerDataHandler {
    @Autowired
    private MessageRouteService messageRouteService;
    @Autowired
    private ChatMessagePersistService messagePersistService;

    @Override
    public void onMessage(AddressInfo routeFromAddress, NettyMessage nettyMessage) {
        ChatMessageDO chatMessageDO = NettyMessageFactory.nettyMessageToClientMessage(nettyMessage);
        messagePersistService.updateRouteDestReceiveTime(chatMessageDO.getMessageId(), new Date());
        messageRouteService.route(chatMessageDO);
    }
}
