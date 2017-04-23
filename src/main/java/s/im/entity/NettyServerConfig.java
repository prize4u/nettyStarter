package s.im.entity;

import java.util.List;

/**
 * Created by za-zhujun on 2017/4/17.
 */
public class NettyServerConfig {

    private AddressInfo selfAddressInfo;
    private List<AddressInfo> targetServerAddressInfo;

    public AddressInfo getSelfAddressInfo() {
        return selfAddressInfo;
    }

    public void setSelfAddressInfo(AddressInfo selfAddressInfo) {
        this.selfAddressInfo = selfAddressInfo;
    }

    public List<AddressInfo> getTargetServerAddressInfo() {
        return targetServerAddressInfo;
    }

    public void setTargetServerAddressInfo(List<AddressInfo> targetServerAddressInfo) {
        this.targetServerAddressInfo = targetServerAddressInfo;
    }
}
