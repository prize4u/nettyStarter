package s.im.server.netty.handler.server;


import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;

import java.util.Date;
import java.util.Set;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public interface OutcomeConnectionRecorder {
    /**
     * 当前主机链接到远程主机信息
     *
     * @return
     */
    Set<HostConnectionDetail> getOutcomeRemoteHostDetail();

    void reocrdOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Date connDate);

    void reocrdOutcomeRemoteLogin(HostConnectionDetail conn);

    void removeOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost);

    boolean existOutcomeConnection(AddressInfo srcHost, AddressInfo destHost);

}
