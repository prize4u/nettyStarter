package s.im.server.netty;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ServerDataHandler;
import s.im.entity.AddressInfo;
import s.im.entity.NettyServerConfig;
import s.im.exception.IMServerException;
import s.im.server.netty.api.IMNettyClient;
import s.im.server.netty.api.IMNettyServer;
import s.im.server.netty.impl.IMNettyClientImpl;
import s.im.server.netty.impl.IMNettyServerImpl;
import s.im.server.netty.impl.NettyServerAddressHelper;
import s.im.service.ChatMessagePersistService;
import s.im.util.Constant;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Created by za-zhujun on 2017/3/30.
 */
@Component
public class NettyServicingStarter implements FactoryBean<IMNettyServer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServicingStarter.class);

    @Autowired
    private NettyServerAddressHelper nettyServerAddressHelper;
    @Autowired
    private ChatMessagePersistService clienChatMessagePersistService;
    @Autowired
    private ServerDataHandler nettyMessageHandler;
    private IMNettyServer nettyServer;
//    private List<IMNettyClient> nettyClients = Lists.newArrayList();

//    @Scheduled(fixedRate = 5000)
//    public void scanServerStatue() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("\n\r").append("*****************************************").append("\n\r").append("server state:").append(nettyServer.getState()).append("\n\r").append("incoming host:").append("\n\r").append(nettyServer.getIncomeRemoteHostDetail()).append("\n\r").append("outcome host:").append("\n\r").append(nettyServer.getOutcomeRemoteHostDetail()).append("\n\r").append("*****************************************").append("\n\r");
//        LOGGER.info(sb.toString());
////        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ schedule job");
//    }

//    @Scheduled(fixedRate = 5000)
//    public void testReconnect() {
//        if (nettyServer.getAddressInfo().getPort()==9091) {
//            IMNettyClient nettyClient = nettyClients.iterator().next();
//
//            try {
//                for (int i = 0; i < 100; i++) {
//                    try {
//                        TimeUnit.SECONDS.sleep(3);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    LOGGER.info("@@@@@@@@@ reconnect " + i);
//                    testReconnect(nettyClient);
//                }
//
//            } catch (Exception e) {
//                LOGGER.error("", e);
//            }
//        }
//    }


    @Override
    public IMNettyServer getObject() throws Exception {
        return nettyServer;
    }

    @Override
    public Class<?> getObjectType() {
        return IMNettyServer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @PostConstruct
    public void init() throws IMServerException {
        NettyServerConfig nettyServerConfig = nettyServerAddressHelper.resolveServerConfig();

        // startNettyServerOn self as netty server
        nettyServer = initAndStartSelfAsNettyServer(nettyServerConfig.getSelfAddressInfo());
        if (!nettyServer.isRunning()) {
            //TODO
        }

//        if (nettyServerConfig.getSelfAddressInfo().getIpAddress().equals("192.168.0.103")) {
        // connection to exist started netty server if exists
        List<AddressInfo> targetServerAddressInfo = nettyServerConfig.getTargetServerAddressInfo();
        if (CollectionUtils.isNotEmpty(targetServerAddressInfo)) {
            // init netty client
            for (AddressInfo addressInfo : targetServerAddressInfo) {
                AddressInfo selfAddressToConnect = new AddressInfo(Constant.SELF_IP_ADDRESS, nettyServerAddressHelper.getLocalPortToConnect());
                IMNettyClient nettyClient = initAndConnectNettyClient(selfAddressToConnect, addressInfo);
//                    nettyClients.add(nettyClient);
                nettyServer.addNettyClient(nettyClient);
            }
        }
//        }
    }

    private IMNettyClient initAndConnectNettyClient(AddressInfo selfAddress, AddressInfo targetAddress) throws IMServerException {
        IMNettyClient nettyClient = new IMNettyClientImpl(selfAddress, targetAddress, nettyServer);
        ((IMNettyClientImpl) nettyClient).setClienChatMessagePersistService(clienChatMessagePersistService);
        ((IMNettyClientImpl) nettyClient).setServerDataHandler(nettyMessageHandler);
        nettyClient.connect();
        return nettyClient;
    }

    @PreDestroy
    public void destory() {
        // stop client
        if (CollectionUtils.isNotEmpty(nettyServer.getAllNettyClient())) {
            for (IMNettyClient nettyClient : nettyServer.getAllNettyClient()) {
                nettyClient.shutdown();
            }
        }

        // stop netty server
        try {
            nettyServer.stop();
        } catch (IMServerException e) {
            e.printStackTrace();
            LOGGER.error("停止服务器错误", e);
        }
    }

    private IMNettyServer initAndStartSelfAsNettyServer(AddressInfo selfAddressInfo) throws IMServerException {
        IMNettyServer nettyServer = new IMNettyServerImpl(selfAddressInfo);
        ((IMNettyServerImpl) nettyServer).setServerDataHandler(this.nettyMessageHandler);
        ((IMNettyServerImpl) nettyServer).setClienChatMessagePersistService(this.clienChatMessagePersistService);
        nettyServer.setWhiteList(nettyServerAddressHelper.getServerWhiteListSet());
        nettyServer.start();
        return nettyServer;
    }



}
