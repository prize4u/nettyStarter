package s.im.exception;

/**
 * Created by za-zhujun on 2017/4/18.
 */
public class IMServerException extends Exception {

    public IMServerException(String message) {
        super(message);
    }

    public IMServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IMServerException(Throwable cause) {
        super(cause);
    }
}
