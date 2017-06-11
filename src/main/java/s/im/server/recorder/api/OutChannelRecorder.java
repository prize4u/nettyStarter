package s.im.server.recorder.api;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface OutChannelRecorder {
    void registOutChannel(AddressInfo remote, Channel channel);

    void deregistOutChannel(AddressInfo remote, Channel channel);

    Channel findOutChannel(AddressInfo remoteAddressInfo);
}
