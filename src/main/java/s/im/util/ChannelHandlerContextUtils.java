package s.im.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import s.im.entity.AddressInfo;

import java.net.InetSocketAddress;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public class ChannelHandlerContextUtils {

    public static AddressInfo getAddressInfo(ChannelHandlerContext ctx) {
        return getAddressInfo(ctx.channel());
    }

    public static AddressInfo getAddressInfo(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        return new AddressInfo(ipAddress);
    }

}
