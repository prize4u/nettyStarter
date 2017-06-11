package s.im.server.netty.api;

import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import s.im.entity.AddressInfo;
import s.im.entity.HostConnectionDetail;
import s.im.server.api.AbstractIMServer;
import s.im.server.recorder.api.InConnectionChannelRecorder;
import s.im.server.recorder.api.OutConnectionChannelRecorder;
import s.im.server.recorder.impl.InConnectionChannelRecorderImpl;
import s.im.server.recorder.impl.OutConnectionChannelRecorderImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public abstract class AbstractIMNettyServer extends AbstractIMServer implements IMNettyServer {
    protected final InConnectionChannelRecorder inConnectionChannelRecorder;
    protected final OutConnectionChannelRecorder outConnectionChannelRecorder;
    protected final ConcurrentHashMap<String, List<IMNettyClient>> nettyClientsMap = new ConcurrentHashMap<>();

    public AbstractIMNettyServer(AddressInfo serverAddressInfo) {
        super(serverAddressInfo);
        this.inConnectionChannelRecorder = new InConnectionChannelRecorderImpl(serverAddressInfo);
        this.outConnectionChannelRecorder = new OutConnectionChannelRecorderImpl(serverAddressInfo);
    }

    @Override
    public void registInChannel(AddressInfo remote, Channel channel) {
        inConnectionChannelRecorder.registInChannel(remote, channel);
    }

    @Override
    public void registOutChannel(AddressInfo remote, Channel channel) {
        outConnectionChannelRecorder.registOutChannel(remote, channel);
    }

    @Override
    public void deregistInChannel(AddressInfo remote, Channel channel) {
        inConnectionChannelRecorder.deregistInChannel(remote, channel);
    }

    @Override
    public void deregistOutChannel(AddressInfo remote, Channel channel) {
        outConnectionChannelRecorder.deregistOutChannel(remote, channel);
    }

    @Override
    public Channel findInChannel(AddressInfo remoteAddressInfo) {
        return inConnectionChannelRecorder.findInChannel(remoteAddressInfo);
    }

    @Override
    public Channel findOutChannel(AddressInfo remoteAddressInfo) {
        return outConnectionChannelRecorder.findOutChannel(remoteAddressInfo);
    }

    @Override
    public Set<HostConnectionDetail> getOutcomeRemoteHostDetail() {
        return outConnectionChannelRecorder.getOutcomeRemoteHostDetail();
    }

    @Override
    public void recordOut(AddressInfo remoteAddress) {
        outConnectionChannelRecorder.recordOut(remoteAddress);
    }

    @Override
    public void removeOut(AddressInfo remoteAddress) {
        outConnectionChannelRecorder.removeOut(remoteAddress);
    }

    @Override
    public boolean existOutConn(AddressInfo remoteAddress) {
        return outConnectionChannelRecorder.existOutConn(remoteAddress);
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return inConnectionChannelRecorder.getIncomeRemoteHostDetail();
    }

    @Override
    public void recordIn(AddressInfo remoteAddress) {
        inConnectionChannelRecorder.removeIn(remoteAddress);
    }

    @Override
    public void removeIn(AddressInfo remoteAddress) {
        inConnectionChannelRecorder.removeIn(remoteAddress);
    }

    @Override
    public boolean existInConn(AddressInfo remoteAddress) {
        return inConnectionChannelRecorder.existInConn(remoteAddress);
    }

    private String buildNettyClientIdentifier(AddressInfo destHost) {
        return this.getAddressInfo() + "-->" + destHost;
    }

    @Override
    public List<IMNettyClient> getNettyClient(AddressInfo destHost) {
        return nettyClientsMap.get(buildNettyClientIdentifier(destHost));
    }

    @Override
    public List<IMNettyClient> getAllNettyClient() {
        List<IMNettyClient> allClients = Lists.newArrayList();
        Collection<List<IMNettyClient>> clients = nettyClientsMap.values();
        if (CollectionUtils.isNotEmpty(clients)) {
            for (List<IMNettyClient> clientListsByDest : clients) {
                allClients.addAll(clientListsByDest);
            }
        }

        return allClients;
    }
}
