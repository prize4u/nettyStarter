package s.im.util;

import com.corundumstudio.socketio.SocketIOClient;
import s.im.entity.AddressInfo;

import java.net.InetSocketAddress;

/**
 * Created by za-zhujun on 2017/5/19.
 */
public class SocketIOClientUtil {

    public static AddressInfo getRemoteAddress(SocketIOClient client) {
        InetSocketAddress socketAddress = (InetSocketAddress) client.getRemoteAddress();
        return new AddressInfo(socketAddress.getHostName(), socketAddress.getPort());
    }

    public static String getClientKey(SocketIOClient client) {
        return client.getSessionId().toString();
    }

    public static String getParam(SocketIOClient client, String paramName) {
        return client.getHandshakeData().getSingleUrlParam(paramName);
    }
}
