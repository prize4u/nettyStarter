package s.im.server.netty.api;

import s.im.entity.AddressInfo;
import s.im.server.api.IMServer;
import s.im.server.recorder.api.HostConnectionChannelRecorder;

import java.util.List;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public interface IMNettyServer extends HostConnectionChannelRecorder, IMServer {

    boolean addNettyClient(IMNettyClient newNettyClient);

    List<IMNettyClient> getAllNettyClient();

    List<IMNettyClient> getNettyClient(AddressInfo destHost);

}
