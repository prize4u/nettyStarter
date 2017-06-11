package s.im.connection.client.api;

import s.im.message.client.ClientChatObject;

/**
 * Created by za-zhujun on 2017/6/8.
 */
public interface ClientDataHandler {
    void onData(ClientChatObject clientChatObject, ClientConnectionChannel clientChannel);
}
