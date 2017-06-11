package s.im.service.mq.api;

/**
 * Created by za-zhujun on 2017/5/16.
 */
public class MessageServiceException extends Exception {
    public MessageServiceException(String message) {
        super(message);
    }

    public MessageServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageServiceException(Throwable cause) {
        super(cause);
    }
}
