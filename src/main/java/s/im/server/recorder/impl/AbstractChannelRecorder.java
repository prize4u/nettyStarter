package s.im.server.recorder.impl;

import s.im.entity.AddressInfo;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public abstract class AbstractChannelRecorder {

    protected abstract String buildChannelKey(AddressInfo src, AddressInfo dest);
}
