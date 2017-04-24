package s.im.server.netty.handler.server;

import com.google.common.collect.Sets;
import io.netty.channel.Channel;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.service.api.ChannelRegistor;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/4/19.
 */
public class HostConnectionRecorderImpl implements HostConnectionRecorder {
    private ConcurrentHashMap<String, HostConnectionDetail> incomeConnectionsMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, HostConnectionDetail> outcomeConnectionsMap = new ConcurrentHashMap<>();
    private ChannelRegistor channelRegistor;

    public HostConnectionRecorderImpl(ChannelRegistor channelRegistor) {
        this.channelRegistor = channelRegistor;
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return Sets.newTreeSet(incomeConnectionsMap.values());
    }

    @Override
    public void reocrdIncomeRemoteLogin(HostConnectionDetail conn) {
        String key = buildKey(conn.getSrcHost(), conn.getDestHost());
        incomeConnectionsMap.put(key, conn);
        channelRegistor.registChannel(conn.getSrcHost(), conn.getDestHost(), conn.getChannel());
    }

    @Override
    public void reocrdIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel, Date loginDate) {
        reocrdIncomeRemoteLogin(new HostConnectionDetail(srcHost, destHost, channel, loginDate));
    }

    private String buildKey(AddressInfo srcHost, AddressInfo destHost) {
        return srcHost + "-->" + destHost;
    }

    @Override
    public void removeIncomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel) {
        String key = buildKey(srcHost, destHost);
        incomeConnectionsMap.remove(key);
        channelRegistor.deregistChannel(srcHost, destHost, channel);
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
    public void reocrdOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel, Date connDate) {
        reocrdOutcomeRemoteLogin(new HostConnectionDetail(srcHost, destHost, channel, connDate));
    }

    @Override
    public void removeOutcomeRemoteLogin(AddressInfo srcHost, AddressInfo destHost, Channel channel) {
        String key = buildKey(srcHost, destHost);
        outcomeConnectionsMap.remove(key);
        channelRegistor.deregistChannel(srcHost, destHost, channel);
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
        channelRegistor.registChannel(conn.getSrcHost(), conn.getDestHost(), conn.getChannel());
    }
}
