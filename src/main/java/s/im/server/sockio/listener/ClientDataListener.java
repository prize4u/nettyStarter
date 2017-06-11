package s.im.server.sockio.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.SocketIOConnection;
import s.im.connection.client.api.ClientDataHandler;
import s.im.message.client.ClientChatObject;

/**
 * Created by za-zhujun on 2017/5/17.
 */
@Component
public class ClientDataListener implements DataListener<ClientChatObject> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDataListener.class);

    @Autowired
    private ClientDataHandler clientDataHandler;

    @Override
    public void onData(SocketIOClient socketIOClient, ClientChatObject clientChatObject, AckRequest ackRequest) throws Exception {
        SocketIOConnection socketIOConnection = new SocketIOConnection(socketIOClient);
        if (ackRequest.isAckRequested()) {
            socketIOConnection.setAckId(ackRequest.getAckId());
        }
        clientDataHandler.onData(clientChatObject, socketIOConnection);
    }

}
