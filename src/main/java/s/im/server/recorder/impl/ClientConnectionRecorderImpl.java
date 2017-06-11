package s.im.server.recorder.impl;

import s.im.connection.client.api.ClientConnectionChannel;
import s.im.entity.AddressInfo;
import s.im.entity.ClientConnectionDetail;
import s.im.server.api.HostAddressable;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.util.Constant;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-zhujun on 2017/6/8.
 */
public class ClientConnectionRecorderImpl implements ClientConnectionRecorder,HostAddressable {

    protected ConcurrentHashMap<String, ClientConnectionDetail> clientMap = new ConcurrentHashMap<>();

    private final AddressInfo selfAddress;

    public ClientConnectionRecorderImpl(AddressInfo selfAddress) {
        this.selfAddress = selfAddress;
    }

    @Override
    public void registClient(ClientConnectionChannel client) {
        String userName = client.get(Constant.LOGIN_USER_NAME);
        AddressInfo remoteAddress = client.getRemoteAddress();
//        recordIn(remoteAddress);
//        LOGGER.info("{} 收到客户端新连接 {}", getAddressInfo(), remoteAddress);
        ClientConnectionDetail newClientConn = new ClientConnectionDetail(remoteAddress, getAddressInfo(), client, new Date());
        clientMap.put(userName, newClientConn);
    }

    @Override
    public void deregisClient(ClientConnectionChannel client) {
        String userName = client.get(Constant.LOGIN_USER_NAME);
//        AddressInfo remoteAddress = client.getRemoteAddress();
//        removeIn(remoteAddress);
//        LOGGER.info("{} 断开客户端新连接 {}", getAddressInfo(), remoteAddress);
        clientMap.remove(userName);
    }

    @Override
    public ClientConnectionChannel findClient(String userName) {
        ClientConnectionDetail clientConnectionDetail = clientMap.get(userName);
        return clientConnectionDetail != null ? clientConnectionDetail.getClient() : null;
    }

    @Override
    public AddressInfo getAddressInfo() {
        return selfAddress;
    }
}
