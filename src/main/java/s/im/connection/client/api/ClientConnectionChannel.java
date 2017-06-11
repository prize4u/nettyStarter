package s.im.connection.client.api;

import s.im.entity.AddressInfo;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.client.ClientChatObject;

/**
 * Created by za-zhujun on 2017/6/7.
 */
public interface ClientConnectionChannel {

    String getKeyIdentifier();

    AddressInfo getRemoteAddress();

    String getSessionId();

    <T> T get(String key);

    void set(String key, Object object);

    void send(ClientEventEnum clientEventEnum, ClientChatObject clientChatObject, ClientEventCallback callback);

    void sendAckData(Object... objs);
}
