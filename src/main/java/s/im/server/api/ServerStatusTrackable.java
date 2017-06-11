package s.im.server.api;

import s.im.entity.AddressInfo;
import s.im.entity.ServerState;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface ServerStatusTrackable {
    boolean isRunning();

    ServerState getServerStatus();

    void setServerStatus(ServerState serverState);
}
