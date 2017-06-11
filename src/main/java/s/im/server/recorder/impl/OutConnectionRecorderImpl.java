package s.im.server.recorder.impl;

import com.google.common.collect.Sets;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.recorder.api.OutConnectionRecorder;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class OutConnectionRecorderImpl extends AbstractConnectionRecorder implements OutConnectionRecorder {
    private ConcurrentHashMap<String, HostConnectionDetail> outcomeConnectionsMap = new ConcurrentHashMap<>();

    public OutConnectionRecorderImpl(AddressInfo selfAddressInfo) {
        super(selfAddressInfo);
    }

    private String buildKey(AddressInfo remoteHost) {
        return selfAddressInfo + "-->" + remoteHost;
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return Sets.newTreeSet(outcomeConnectionsMap.values());
    }

    @Override
    public void recordOut(AddressInfo remoteAddress) {
        String key = buildKey(remoteAddress);
        outcomeConnectionsMap.put(key, new HostConnectionDetail(selfAddressInfo, remoteAddress, new Date()));
    }

    @Override
    public void removeOut(AddressInfo remoteAddress) {
        String key = buildKey(remoteAddress);
        outcomeConnectionsMap.remove(key);
    }

    @Override
    public boolean existOutConn(AddressInfo remoteAddress) {
        String key = buildKey(remoteAddress);
        return outcomeConnectionsMap.contains(key);
    }

}
