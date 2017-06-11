package s.im.server.recorder.api;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface InChannelRecorder {
    void registInChannel(AddressInfo remote, Channel channel);

    void deregistInChannel(AddressInfo remote, Channel channel);

    Channel findInChannel(AddressInfo remoteAddressInfo);
}
