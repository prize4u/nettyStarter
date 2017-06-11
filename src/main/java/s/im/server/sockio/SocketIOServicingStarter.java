package s.im.server.sockio;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ClientDataHandler;
import s.im.entity.AddressInfo;
import s.im.entity.NettyServerConfig;
import s.im.exception.IMServerException;
import s.im.server.netty.impl.NettyServerAddressHelper;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.server.recorder.impl.ClientConnectionRecorderImpl;
import s.im.server.sockio.api.IMSocketIOServer;
import s.im.server.sockio.impl.IMSocketIOServerImpl;
import s.im.server.sockio.listener.ClientDataListener;
import s.im.server.sockio.listener.SocketIOConnectionListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by za-zhujun on 2017/5/17.
 */
@Component
public class SocketIOServicingStarter implements FactoryBean<IMSocketIOServer> {

    @Autowired
    private NettyServerAddressHelper nettyServerAddressHelper;
    @Autowired
    private ClientDataListener clientDataListener;
//    @Autowired
//    private ClientDataHandler clientDataHandler;

    @Autowired
    private SocketIOConnectionListener connectionListener;

    @Value("${socketio.starting.port}")
    private int socketioConnPort;

    private IMSocketIOServer imSocketIOServer;
    @Autowired
    private ClientConnectionRecorder clientConnectionRecorder;

    @PostConstruct
    public void init() throws IMServerException {
        NettyServerConfig nettyServerConfig = nettyServerAddressHelper.resolveServerConfig();
        AddressInfo selfAddress = nettyServerConfig.getSelfAddressInfo();
        imSocketIOServer = new IMSocketIOServerImpl(new AddressInfo(selfAddress.getIpAddress(), socketioConnPort), clientConnectionRecorder);
        ((IMSocketIOServerImpl) imSocketIOServer).setDataListener(this.clientDataListener);
        ((IMSocketIOServerImpl) imSocketIOServer).setConnectionListener(this.connectionListener);
        imSocketIOServer.start();
    }

    @Override
    public IMSocketIOServer getObject() throws Exception {
        return imSocketIOServer;
    }

    @Override
    public Class<?> getObjectType() {
        return IMSocketIOServer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @PreDestroy
    public void destory() {
        try {
            imSocketIOServer.stop();
        } catch (IMServerException e) {
            e.printStackTrace();
        }
    }


}
