package s.im.server.sockio.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.SocketIOConnection;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.connection.client.api.ClientConnectionHandler;

/**
 * Created by za-zhujun on 2017/5/19.
 */
@Component
public class SocketIOConnectionListener implements ConnectListener, DisconnectListener {
    //    private static final Logger LOGGER = LoggerFactory.getLogger(SocketIOConnectionListener.class);
    @Autowired
    private ClientConnectionHandler clientConnectionHandler;

    @Override
    public void onConnect(SocketIOClient client) {
        ClientConnectionChannel socketIOConnection = new SocketIOConnection(client);
        clientConnectionHandler.onConnect(socketIOConnection);
    }

    @Override
    public void onDisconnect(SocketIOClient client) {
        ClientConnectionChannel socketIOConnection = new SocketIOConnection(client);
        clientConnectionHandler.onDisconnect(socketIOConnection);
    }
}
