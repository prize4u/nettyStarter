package s.im.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.connection.client.SocketIOConnection;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.entity.AddressInfo;
import s.im.entity.ClientConnectionDetail;
import s.im.entity.HostConnectionDetail;
import s.im.server.api.AbstractIMServer;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.server.recorder.api.InConnectionRecorder;
import s.im.server.recorder.impl.InConnectionRecorderImpl;
import s.im.server.sockio.api.IMSocketIOServer;
import s.im.util.Constant;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public abstract class AbstractIMSockIOServer extends AbstractIMServer implements IMSocketIOServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIMSockIOServer.class);
    protected final InConnectionRecorder inConnectionRecorder;
    protected final ClientConnectionRecorder clientConnectionRecorder;
//    protected ConcurrentHashMap<String, ClientConnectionDetail> clientMap = new ConcurrentHashMap<>();

    public AbstractIMSockIOServer(AddressInfo serverAddressInfo, ClientConnectionRecorder clientConnectionRecorder) {
        super(serverAddressInfo);
        this.inConnectionRecorder = new InConnectionRecorderImpl(serverAddressInfo);
        this.clientConnectionRecorder = clientConnectionRecorder;
    }

    @Override
    public Set<HostConnectionDetail> getIncomeRemoteHostDetail() {
        return inConnectionRecorder.getIncomeRemoteHostDetail();
    }

    @Override
    public void recordIn(AddressInfo remoteAddress) {
        inConnectionRecorder.recordIn(remoteAddress);
    }

    @Override
    public void removeIn(AddressInfo remoteAddress) {
        inConnectionRecorder.removeIn(remoteAddress);
    }

    @Override
    public boolean existInConn(AddressInfo remoteAddress) {
        return inConnectionRecorder.existInConn(remoteAddress);
    }

    @Override
    public void registClient(ClientConnectionChannel client) {
        String userName = client.get(Constant.LOGIN_USER_NAME);
        AddressInfo remoteAddress = client.getRemoteAddress();
        LOGGER.info("用户{}登录，sessionID: {}, clientID: {}", userName, client.getSessionId(), ((SocketIOConnection)
                client).getClient().hashCode());
        recordIn(remoteAddress);
        clientConnectionRecorder.registClient(client);
    }

    @Override
    public void deregisClient(ClientConnectionChannel client) {
        AddressInfo remoteAddress = client.getRemoteAddress();
        removeIn(remoteAddress);
        LOGGER.info("{} 断开客户端新连接 {}", getAddressInfo(), remoteAddress);
        clientConnectionRecorder.deregisClient(client);
    }

    @Override
    public ClientConnectionChannel findClient(String userName) {
        return clientConnectionRecorder.findClient(userName);
    }
}
