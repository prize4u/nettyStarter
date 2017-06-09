package s.im.server.recorder.api;


import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;

import java.util.Date;
import java.util.Set;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public interface OutConnectionRecorder {
    /**
     * 当前主机链接到远程主机信息
     *
     * @return
     */
    Set<HostConnectionDetail> getOutcomeRemoteHostDetail();

    void recordOut(AddressInfo remoteAddress);

    void removeOut(AddressInfo remoteAddress);

    boolean existOutConn(AddressInfo remoteAddress);

//    void recordOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel, Date connDate);
//
//    void recordOutcomeRemoteLogin(HostConnectionDetail conn);
//
//    void removeOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel);
//
//    boolean existOutcomeConnection(AddressInfo srcHost, AddressInfo destHost);

}
