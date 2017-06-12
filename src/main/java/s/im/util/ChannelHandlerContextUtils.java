package s.im.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import s.im.entity.AddressInfo;

import java.net.InetSocketAddress;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public class ChannelHandlerContextUtils {

    public static AddressInfo getAddressInfo(AddressInfo selfAddress, ChannelHandlerContext ctx) {
        return getAddressInfo(selfAddress, ctx.channel());
    }

    public static AddressInfo getAddressInfo(AddressInfo selfAddress, Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();
        int targetPort;
        if (selfAddress.getPort() == 9090) {
            targetPort = 9091;
        } else {
            targetPort = 9090;
        }
        return new AddressInfo(ipAddress, targetPort);
    }

}
