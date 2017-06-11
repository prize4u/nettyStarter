package s.im.connection.client;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.connection.client.api.ClientEventCallback;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.client.ClientChatObject;
import s.im.util.SocketIOClientUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by za-zhujun on 2017/6/7.
 */
public class SocketIOConnection implements ClientConnectionChannel {
    private final SocketIOClient client;
    private Long ackId;

    public SocketIOConnection(SocketIOClient client) {
        this.client = client;
    }

    @Override
    public String getSessionId() {
        return client.getSessionId().toString();
    }

    @Override
    public <T> T get(String key) {
        T param = (T) SocketIOClientUtil.getParam(client, key);
        if (param == null) {
            param = client.get(key);
        }
        return param;
    }

    @Override
    public void set(String key, Object object) {
        client.set(key, object);
    }

    @Override
    public AddressInfo getRemoteAddress() {
        return SocketIOClientUtil.getRemoteAddress(this.client);
    }

    @Override
    public String getKeyIdentifier() {
        return getSessionId();
    }

    @Override
    public void sendAckData(Object... objs) {
        List<Object> args = Arrays.asList(objs);
        Packet ackPacket = new Packet(PacketType.MESSAGE);
        ackPacket.setSubType(PacketType.ACK);
        ackPacket.setAckId(ackId);
        ackPacket.setData(args);
        client.send(ackPacket);
    }

    @Override
    public void send(ClientEventEnum clientEventEnum, ClientChatObject clientChatObject, ClientEventCallback callback) {
        client.sendEvent(clientEventEnum.getCode(), new AckCallback<ClientChatObject>(ClientChatObject.class) {
            @Override
            public void onSuccess(ClientChatObject result) {
                callback.onSuccess(result);
            }

            @Override
            public void onTimeout() {
                callback.onTimeout(clientChatObject);
            }
        }, clientChatObject);
    }

    public void setAckId(Long ackId) {
        this.ackId = ackId;
    }

    public SocketIOClient getClient() {
        return client;
    }
}
