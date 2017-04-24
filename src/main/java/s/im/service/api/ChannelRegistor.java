package s.im.service.api;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;

/**
 * Created by za-zhujun on 2017/4/24.
 */
public interface ChannelRegistor {

    boolean registChannel(AddressInfo src, AddressInfo remote, Channel channel);

    boolean deregistChannel(AddressInfo src, AddressInfo remote, Channel channel);

    Channel find(AddressInfo src, AddressInfo remote);
}
