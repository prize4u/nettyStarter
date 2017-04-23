package s.im.exception;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class NettyServerException extends Exception {

    public NettyServerException(String message) {
        super(message);
    }

    public NettyServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyServerException(Throwable cause) {
        super(cause);
    }
}
