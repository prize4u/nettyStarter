package s.im.server.recorder.impl;

import com.google.common.collect.Sets;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.recorder.api.InConnectionRecorder;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class InConnectionRecorderImpl extends AbstractConnectionRecorder implements InConnectionRecorder {
    private ConcurrentHashMap<String, HostConnectionDetail> incomeConnectionsMap = new ConcurrentHashMap<>();

    public InConnectionRecorderImpl(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return Sets.newTreeSet(incomeConnectionsMap.values());
    }

    private String buildKey(AddressInfo remoteHost) {
        return remoteHost + "-->" + selfAddressInfo;
    }

    @Override
    public void recordIn(AddressInfo remoteAddress) {
        String key = buildKey(remoteAddress);
        incomeConnectionsMap.put(key, new HostConnectionDetail(remoteAddress, selfAddressInfo, new Date()));
    }

    @Override
    public void removeIn(AddressInfo remoteAddress) {
        String key = buildKey(remoteAddress);
        incomeConnectionsMap.remove(key);
    }

    @Override
    public boolean existInConn(AddressInfo remoteAddress) {
        String key = buildKey(remoteAddress);
        return incomeConnectionsMap.containsKey(key);
    }

}
