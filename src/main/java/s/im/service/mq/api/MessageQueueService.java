package s.im.service.mq.api;

import org.springframework.amqp.core.Message;

/**
 *
 *
 * Created by za-zhujun on 2017/5/16.
 */
public interface MessageQueueService {

    void publish(String queueKey, Object object) throws MessageServiceException;

    Message retrieveNext(String queueKey) throws MessageServiceException;


}
