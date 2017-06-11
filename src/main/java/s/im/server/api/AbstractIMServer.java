package s.im.server.api;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s.im.entity.AddressInfo;
import s.im.entity.ServerState;
import s.im.exception.IMServerException;
import s.im.server.netty.impl.AbstractStatefulHostAddress;

import java.util.Set;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public abstract class AbstractIMServer extends AbstractStatefulHostAddress implements IMServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIMServer.class);
    protected Set<String> whiteListSet = Sets.newHashSet();

    public AbstractIMServer(AddressInfo addressInfo) {
        super(addressInfo);
    }

    @Override
    public synchronized void restart() throws IMServerException {
        // stop if running
        if (isRunning()) {
            setServerStatus(ServerState.Restarting);
            try {
                doStopServer();
            } catch (Exception e) {
                LOGGER.error("重启服务器....停止服务器失败！", e);
                setServerStatus(ServerState.Stopped);
                throw new IMServerException(e);
            }
        }
        LOGGER.info("重启服务器....服务器在地址 {} 上已停止:{}", this.getAddressInfo(), !isRunning());

        // start
        if (!isRunningOrStarting()) {
            try {
                doStartServer();
                setServerStatus(ServerState.Running);
            } catch (Exception e) {
                LOGGER.error("重启服务器....启动服务器失败！", e);
                setServerStatus(ServerState.Stopped);
                throw new IMServerException(e);
            }
        }
        LOGGER.info("重启服务器....服务器在地址 {} 上启动成功: {}", this.getAddressInfo(), isRunning());
    }

    @Override
    public boolean canAcceptedHost(String ipAddress) {
        return whiteListSet.contains(ipAddress);
    }

    @Override
    public void setWhiteList(Set<String> whiteList) {
        this.whiteListSet = whiteList;
    }

    @Override
    public Set<String> getWhiteList() {
        return this.whiteListSet;
    }

    @Override
    public synchronized void start() throws IMServerException {
        if (!isRunningOrStarting()) {
            setServerStatus(ServerState.Starting);
            try {
                doStartServer();
                setServerStatus(ServerState.Running);
            } catch (Exception e) {
                LOGGER.error("启动服务器失败！", e);
                setServerStatus(ServerState.Stopped);
                throw new IMServerException(e);
            }
        } else {
            LOGGER.info("服务器在地址 {} 上已启动", this.getAddressInfo());
        }
    }

    private boolean isRunningOrStarting() {
        return ServerState.Running.equals(serverState) || ServerState.Starting.equals(serverState) || ServerState.Restarting.equals(serverState);
    }

    @Override
    public synchronized void stop() throws IMServerException {
        if (isRunning()) {
            setServerStatus(ServerState.Stopping);
            try {
                doStopServer();
                setServerStatus(ServerState.Stopped);
            } catch (Exception e) {
                LOGGER.error("停止服务器失败！", e);
                setServerStatus(ServerState.Stopped);
                throw new IMServerException(e);
            }
        } else {
            LOGGER.info("服务器在地址 {} 上已停止", this.getAddressInfo());
        }
    }

    protected abstract void doStopServer();

    protected abstract void doStartServer();
}
