package s.im.connection.client.api;

import s.im.message.client.ClientChatObject;

/**
 * Created by za-zhujun on 2017/6/7.
 */
public interface ClientEventCallback {
    void onSuccess(ClientChatObject clientChatObject);

    void onFailed(ClientChatObject clientChatObject);

    void onTimeout(ClientChatObject clientChatObject);
}
