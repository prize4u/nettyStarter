package s.im.utils;

import io.netty.channel.Channel;

/**
 * Created by za-zhujun on 2017/4/24.
 */
public class ChannelUtils {

    public static boolean isOpenAndActive(Channel channel) {
        return isOpen(channel) && isActive(channel);
    }

    private static boolean isActive(Channel channel) {
        return channel != null && channel.isActive();
    }

    private static boolean isOpen(Channel channel) {
        return channel != null && channel.isOpen();
    }
}
