package s.im.server.api;

import s.im.exception.IMServerException;

/**
 * Created by za-zhujun on 2017/5/17.
 */
public interface ServerLifecycle {

    void start() throws IMServerException;

//    void startAsyn(NettyOperationCallback callback);

    void stop() throws IMServerException;

    void restart() throws IMServerException;
}
