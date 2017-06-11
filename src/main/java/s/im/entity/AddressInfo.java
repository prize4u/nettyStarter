package s.im.entity;

import java.io.Serializable;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class AddressInfo implements Serializable, Comparable<AddressInfo> {
    private final String ipAddress;
    private int port;

    public AddressInfo(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public AddressInfo(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int compareTo(AddressInfo o) {
        int result = this.ipAddress.compareTo(o.ipAddress);
        if (result == 0) {
            return Integer.valueOf(this.getPort()).compareTo(o.port);
        } else {
            return result;
        }
    }

    @Override
    public String toString() {
        return ipAddress+":"+port;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AddressInfo)) {
            return false;
        }
        AddressInfo addressInfo = (AddressInfo) o;
        return addressInfo.ipAddress.equals(this.ipAddress)
                && addressInfo.port == this.port;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + ipAddress.hashCode();
        result = 37 * result + port;
        return result;
    }
}
