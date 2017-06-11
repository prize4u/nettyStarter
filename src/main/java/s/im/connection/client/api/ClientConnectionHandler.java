package s.im.connection.client.api;

/**
 * Created by za-zhujun on 2017/6/8.
 */
public interface ClientConnectionHandler {
    void onConnect(ClientConnectionChannel clientChannel);

    void onDisconnect(ClientConnectionChannel clientChannel);
}
