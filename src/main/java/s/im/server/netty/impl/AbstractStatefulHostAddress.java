package s.im.server.netty.impl;

import s.im.entity.AddressInfo;
import s.im.entity.ServerState;
import s.im.server.api.HostAddressable;
import s.im.server.api.ServerStatusTrackable;

/**
 * Created by za-zhujun on 2017/5/19.
 */
public abstract class AbstractStatefulHostAddress implements HostAddressable, ServerStatusTrackable {
    private final AddressInfo addressInfo;
    protected ServerState serverState = ServerState.Stopped;

    public AbstractStatefulHostAddress(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    @Override
    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    @Override
    public boolean isRunning() {
        return ServerState.Running.equals(serverState);
    }

    @Override
    public ServerState getServerStatus() {
        return serverState;
    }

    @Override
    public void setServerStatus(ServerState serverState) {
        this.serverState = serverState;
    }
}
