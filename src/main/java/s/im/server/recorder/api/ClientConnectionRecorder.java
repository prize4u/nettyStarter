package s.im.server.recorder.api;

import s.im.connection.client.api.ClientConnectionChannel;

/**
 * Created by za-zhujun on 2017/6/8.
 */
public interface ClientConnectionRecorder {
    void registClient(ClientConnectionChannel client);

    void deregisClient(ClientConnectionChannel client);

    ClientConnectionChannel findClient(String userName);
}
