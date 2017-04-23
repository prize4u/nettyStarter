package s.im.server.netty;

import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.entity.NettyServerConfig;
import s.im.server.netty.api.IMNettyClient;
import s.im.server.netty.api.IMNettyServer;
import s.im.server.netty.impl.IMNettyClientImpl;
import s.im.server.netty.impl.IMNettyServerImpl;
import s.im.server.netty.impl.NettyServerAddressHelper;
import s.im.utils.Constant;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by za-zhujun on 2017/3/30.
 */
@Component
public class ServicingStarter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServicingStarter.class);

    @Autowired
    private NettyServerAddressHelper nettyServerAddressHelper;
    private IMNettyServer nettyServer;
    private List<IMNettyClient> nettyClients = Lists.newArrayList();

    @Scheduled(fixedRate = 5000)
    public void scanServerStatue() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\r").append("*****************************************").append("\n\r").append("server state:").append(nettyServer.getState()).append("\n\r").append("incoming host:").append("\n\r").append(nettyServer.getIncomeRemoteHostDetail()).append("\n\r").append("outcome host:").append("\n\r").append(nettyServer.getOutcomeRemoteHostDetail()).append("\n\r").append("*****************************************").append("\n\r");
        LOGGER.info(sb.toString());
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ schedule job");
    }

    @Scheduled(fixedRate = 5000)
    public void testReconnect() {
        if (nettyServer.getAddressInfo().getPort()==9091) {
            IMNettyClient nettyClient = nettyClients.iterator().next();

            try {
                for (int i = 0; i < 100; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("@@@@@@@@@ reconnect " + i);
                    testReconnect(nettyClient);
                }

            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
    }

    @PostConstruct
    public void init() {
        NettyServerConfig nettyServerConfig = nettyServerAddressHelper.resolveServerConfig();

        // startNettyServerOn self as netty server
        nettyServer = initAndStartSelfAsNettyServer(nettyServerConfig.getSelfAddressInfo());
        if (!nettyServer.isRunning()) {
            //TODO
        }

        // connection to exist started netty server if exists
        List<AddressInfo> targetServerAddressInfo = nettyServerConfig.getTargetServerAddressInfo();
        if (CollectionUtils.isNotEmpty(targetServerAddressInfo)) {
            // init netty client
            for (AddressInfo addressInfo : targetServerAddressInfo) {
                AddressInfo selfAddressToConnect = new AddressInfo(Constant.SELF_IP_ADDRESS, nettyServerAddressHelper.getLocalPortToConnect());
                IMNettyClient nettyClient = initAndConnectNettyClient(selfAddressToConnect, addressInfo);
                nettyClients.add(nettyClient);
            }
        }
    }

    private void testReconnect(IMNettyClient nettyClient) {
//
//
//        System.out.println(nettyClient.isConnected());
        Channel channel = nettyClient.getChannel();
//        System.out.println(nettyClient.getChannel() == null);
        if (channel != null) {
            boolean isOpenAndActive = nettyClient.getChannel().isOpen() && nettyClient.getChannel().isActive();
            if (isOpenAndActive) {
                nettyClient.disconnect();
                LOGGER.info("sleep 3 sec before start connect");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nettyClient.connect();
            }
        }

    }

    private IMNettyClient initAndConnectNettyClient(AddressInfo selfAddress, AddressInfo targetAddress) {
        IMNettyClient nettyClient = new IMNettyClientImpl(nettyServer, selfAddress, targetAddress);
        nettyClient.connect();
        return nettyClient;
    }

    @PreDestroy
    public void destory() {
        // stop client
        if (CollectionUtils.isNotEmpty(nettyClients)) {
            for (IMNettyClient nettyClient : nettyClients) {
                nettyClient.shutdown();
            }
        }

        // stop netty server
        nettyServer.stop();
    }

    private IMNettyServer initAndStartSelfAsNettyServer(AddressInfo selfAddressInfo) {
        IMNettyServer nettyServer = new IMNettyServerImpl(selfAddressInfo, nettyServerAddressHelper.getServerWhiteListSet());
        nettyServer.start();
        return nettyServer;
    }


}
