package s.im.entity;

import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class HostConnectionChannelDetail extends HostConnectionDetail implements Serializable {
    private Channel channel;

    public HostConnectionChannelDetail(AddressInfo srcHost, AddressInfo destHost, Date connDate, Channel channel) {
        super(srcHost, destHost, connDate);
        this.channel = channel;
    }

    @Override
    public int compareTo(HostConnectionDetail o) {
        return super.compareTo(o);
    }

    @Override
    public String toString() {
        return super.toString() + " with channel: " + this.channel;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = super.equals(o);
        if (!equals) {
            return false;
        }

        HostConnectionChannelDetail conn = (HostConnectionChannelDetail) o;
        return conn.channel.equals(this.channel);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 37 * result + channel.hashCode();
        return result;
    }


}
