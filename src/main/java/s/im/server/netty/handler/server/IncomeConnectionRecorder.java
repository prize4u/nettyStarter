package s.im.server.netty.handler.server;

import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;

import java.util.Date;
import java.util.Set;

/**
 *
 * Created by za-zhujun on 2017/4/19.
 */
public interface IncomeConnectionRecorder {
    /**
     * 登录到当前主机的远程主机地址信息及登录时间
     *
     * @return
     */
    Set<HostConnectionDetail> getIncomeRemoteHostDetail();

    void reocrdIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Date loginDate);

    void reocrdIncomeRemoteLogin(HostConnectionDetail conn);

    void removeIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost);

    boolean existIncomeConnection(AddressInfo srcHost, AddressInfo destHost);

}
