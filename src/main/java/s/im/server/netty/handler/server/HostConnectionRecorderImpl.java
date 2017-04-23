package s.im.server.netty.handler.server;

import com.google.common.collect.Sets;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public class HostConnectionRecorderImpl implements HostConnectionRecorder {
    private ConcurrentHashMap<String, HostConnectionDetail> incomeConnectionsMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, HostConnectionDetail> outcomeConnectionsMap = new ConcurrentHashMap<>();

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return Sets.newTreeSet(incomeConnectionsMap.values());
    }

    @Override
    public void reocrdIncomeRemoteLogin(HostConnectionDetail conn) {
        String key = buildKey(conn.getSrcHost(), conn.getDestHost());
        incomeConnectionsMap.put(key, conn);
    }

    @Override
    public void reocrdIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Date loginDate) {
        reocrdIncomeRemoteLogin(new HostConnectionDetail(srcHost, destHost, loginDate));
    }

    private String buildKey(AddressInfo srcHost, AddressInfo destHost) {
        return srcHost + "-->" + destHost;
    }

    @Override
    public void removeIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost) {
        String key = buildKey(srcHost, destHost);
        incomeConnectionsMap.remove(key);
    }

    @Override
    public boolean existIncomeConnection(AddressInfo srcHost, AddressInfo destHost) {
        String key = buildKey(srcHost, destHost);
        return incomeConnectionsMap.contains(key);
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return Sets.newTreeSet(outcomeConnectionsMap.values());
    }

    @Override
    public void reocrdOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Date connDate) {
        reocrdOutcomeRemoteLogin(new HostConnectionDetail(srcHost, destHost, connDate));
    }

    @Override
    public void removeOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost) {
        String key = buildKey(srcHost, destHost);
        outcomeConnectionsMap.remove(key);
    }

    @Override
    public boolean existOutcomeConnection(AddressInfo srcHost, AddressInfo destHost) {
        String key = buildKey(srcHost, destHost);
        return outcomeConnectionsMap.contains(key);
    }

    @Override
    public void reocrdOutcomeRemoteLogin(HostConnectionDetail conn) {
        String key = buildKey(conn.getSrcHost(), conn.getDestHost());
        outcomeConnectionsMap.put(key, conn);
    }
}
