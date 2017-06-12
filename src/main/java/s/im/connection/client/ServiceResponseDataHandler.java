package s.im.connection.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.server.NettyMessage;
import s.im.service.ChatMessagePersistService;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/6/12.
 */
@Component
public class ServiceResponseDataHandler implements ServerDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceResponseDataHandler.class);

    private ChatMessagePersistService clienChatMessagePersistService;

    @Override
    public void onMessage(AddressInfo routeFromAddress, NettyMessage nettyMessage) {
        if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.SERVICE_RESP.value()) {
            String ackMessageId = (String) nettyMessage.getBody();
            LOGGER.info("收到来自 {} 的确认消息，消息ID {}", routeFromAddress, ackMessageId);

            // update
            clienChatMessagePersistService.updateRouteAckTime(ackMessageId, new Date());
        }
    }
}
