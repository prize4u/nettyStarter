package s.im.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public class HostConnectionDetail implements Serializable, Comparable<HostConnectionDetail> {
    private final AddressInfo srcHost;
    private final AddressInfo destHost;
    private final Date connDate;

    public HostConnectionDetail(AddressInfo srcHost, AddressInfo destHost, Date connDate) {
        this.srcHost = srcHost;
        this.destHost = destHost;
        this.connDate = connDate;
    }

    public AddressInfo getSrcHost() {
        return srcHost;
    }

    public AddressInfo getDestHost() {
        return destHost;
    }

    public Date getConnDate() {
        return connDate;
    }

    @Override
    public int compareTo(HostConnectionDetail o) {
        int result = this.connDate.compareTo(o.connDate);
        if (result == 0) {
            result = this.srcHost.compareTo(o.srcHost);
            if (result == 0) {
                result = this.destHost.compareTo(o.destHost);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return this.srcHost + "-->" + this.destHost + " at " + connDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HostConnectionDetail)) {
            return false;
        }
        HostConnectionDetail conn = (HostConnectionDetail) o;
        return conn.srcHost.equals(this.srcHost) && conn.destHost.equals(this.destHost) && conn.connDate.equals(this.connDate)
                ;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + srcHost.hashCode();
        result = 37 * result + destHost.hashCode();
        result = 37 * result + connDate.hashCode();
        return result;
    }


}
