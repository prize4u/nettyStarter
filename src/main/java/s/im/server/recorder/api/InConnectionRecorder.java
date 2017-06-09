package s.im.server.recorder.api;

import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;

import java.util.Date;
import java.util.Set;

/**
 *
 * Created by za-zhujun on 2017/4/19.
 */
public interface InConnectionRecorder {
    /**
     * 登录到当前主机的远程主机地址信息及登录时间
     *
     * @return
     */
    Set<HostConnectionDetail> getIncomeRemoteHostDetail();

    void recordIn(AddressInfo remoteAddress);

    void removeIn(AddressInfo remoteAddress);

    boolean existInConn(AddressInfo remoteAddress);


}
