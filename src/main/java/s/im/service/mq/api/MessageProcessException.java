package s.im.service.mq.api;

/**
 * Created by za-zhujun on 2017/5/16.
 */
public class MessageProcessException extends Exception {
    public MessageProcessException() {
        super();
    }

    public MessageProcessException(String message) {
        super(message);
    }

    public MessageProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessException(Throwable cause) {
        super(cause);
    }
}
