package s.im.server.api;

import java.util.Set;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface HostConnectWise {
    boolean canAcceptedHost(String ipAddress);

    void setWhiteList(Set<String> whiteList);

    Set<String> getWhiteList();
}
