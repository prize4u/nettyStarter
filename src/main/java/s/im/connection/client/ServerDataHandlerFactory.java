package s.im.connection.client;

import org.springframework.beans.factory.annotation.Autowired;
import s.im.connection.client.api.ServerDataHandler;
import s.im.message.MessageType;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/6/12.
 */
public class ServerDataHandlerFactory {
    private static Map<MessageType, ServerDataHandler> handlerMap = new ConcurrentHashMap<>();

    @Autowired
    private ServiceRequestDataHandler requestDataHandler;
    @Autowired
    private ServiceResponseDataHandler responseDataHandler;

    @PostConstruct
    public void init() {
        handlerMap.put(MessageType.SERVICE_REQ, requestDataHandler);
        handlerMap.put(MessageType.SERVICE_RESP, responseDataHandler);
    }


    public static ServerDataHandler getHandler(MessageType messageType) {
        ServerDataHandler serverDataHandler = handlerMap.get(messageType);
        return serverDataHandler;
    }

    public static boolean isValidMessageType(byte value) {
        return MessageType.getMessageType(value) != null;
    }

}
