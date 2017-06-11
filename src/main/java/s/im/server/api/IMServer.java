package s.im.server.api;

import s.im.entity.AddressInfo;
import s.im.server.recorder.api.HostConnectionChannelRecorder;
import s.im.server.recorder.api.InConnectionRecorder;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface IMServer extends ServerLifecycle, ServerStatusTrackable, HostConnectWise, HostAddressable {

}
