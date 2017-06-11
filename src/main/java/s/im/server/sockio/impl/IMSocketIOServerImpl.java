package s.im.server.sockio.impl;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.client.ClientChatObject;
import s.im.server.impl.AbstractIMSockIOServer;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.server.sockio.listener.SocketIOConnectionListener;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public class IMSocketIOServerImpl extends AbstractIMSockIOServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(IMSocketIOServerImpl.class);

    private Configuration serverConfig;
    private SocketIOServer serverInstance;
    private DataListener dataListener;
    private SocketIOConnectionListener connectionListener;

    public IMSocketIOServerImpl(AddressInfo serverAddressInfo, ClientConnectionRecorder clientConnectionRecorder) {
//        super(serverAddressInfo, inConnectionRecorder);
        super(serverAddressInfo, clientConnectionRecorder);
        initSocketIOServerConfig();
    }

    private void initSocketIOServerConfig() {
        serverConfig = new Configuration();
        serverConfig.setPort(getAddressInfo().getPort());
    }

    @Override
    protected void doStopServer() {
        serverInstance.stop();
        serverConfig = null;
        serverInstance = null;
    }

    @Override
    protected void doStartServer() {
        serverInstance = new SocketIOServer(serverConfig);
        serverInstance.addConnectListener(connectionListener);
        serverInstance.addEventListener(ClientEventEnum.CHAT_EVENT.getCode(), ClientChatObject.class, dataListener);
        serverInstance.start();
        LOGGER.info("socketIO 服务器在地址 {} 启动成功", this.getAddressInfo());
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public void setConnectionListener(SocketIOConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
}
