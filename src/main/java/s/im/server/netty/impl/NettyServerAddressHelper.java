package s.im.server.netty.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.entity.NettyServerConfig;
import s.im.utils.Constant;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by za-zhujun on 2017/4/17.
 */
@Component
public class NettyServerAddressHelper implements InitializingBean {

    @Value("${netty.server.servicing.address}")
    private String servicingAddress;
    @Value("${netty.server.selfport}")
    private int selfServicingPort;
    @Value("${netty.server.local.port}")
    private int localPortToConnect;
    @Value("${netty.server.white.list}")
    private String serverWhiteList;

    private Set<String> serverWhiteListSet = new HashSet<>();

    public NettyServerConfig resolveServerConfig() {
        NettyServerConfig config = new NettyServerConfig();
        List<AddressInfo> serverAddressInfos = parseServiceAddress();
        AddressInfo selfAddress = selfServicingAddress();
        config.setSelfAddressInfo(selfAddress);

        serverAddressInfos.remove(selfAddress);
        config.setTargetServerAddressInfo(serverAddressInfos);
        return config;
    }

    private AddressInfo selfServicingAddress() {
        return new AddressInfo(Constant.SELF_IP_ADDRESS, selfServicingPort);
    }

    private List<AddressInfo> parseServiceAddress() {
        List<AddressInfo> results = Lists.newArrayList();
        String[] addressAndPort = servicingAddress.split(",");
        for (String address : addressAndPort) {
            String[] ipAndPortInfo = address.split(":");
            results.add(new AddressInfo(ipAndPortInfo[0], Integer.valueOf(ipAndPortInfo[1])));
        }
        Collections.sort(results);
        return results;
    }

    public int getLocalPortToConnect() {
        return localPortToConnect;
    }

    public Set<String> getServerWhiteListSet() {
        return serverWhiteListSet;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(serverWhiteList)) {
            String[] servers = serverWhiteList.split(",");
            for (String server : servers) {
                serverWhiteListSet.add(StringUtils.trimToEmpty(server));
            }
        }
    }
}
