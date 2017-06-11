package s.im.server.sockio.api;

import s.im.connection.client.api.ClientConnectionChannel;
import s.im.server.api.IMServer;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.server.recorder.api.InConnectionRecorder;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface IMSocketIOServer extends InConnectionRecorder, IMServer, ClientConnectionRecorder {
//    void registClient(ClientConnectionChannel client);
//
//    void deregisClient(ClientConnectionChannel client);
//
//    ClientConnectionChannel findClient(String clientKey);
}
