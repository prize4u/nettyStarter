package s.im.service.mq.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.service.mq.api.MessageProducer;
import s.im.service.mq.api.MessageRetrieve;
import s.im.service.mq.api.MessageQueueService;
import s.im.service.mq.api.MessageServiceException;

/**
 * Created by za-zhujun on 2017/5/16.
 */
@Component
public class DefaultMessageQueueService implements MessageQueueService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageQueueService.class);
    @Autowired
    private MessageRetrieve messageRetrieve;
    @Autowired
    private MessageProducer messageProducer;

//    public DefaultMessageQueueService(MessageRetrieve messageRetrieve, MessageProducer messageProducer) {
//        this.messageRetrieve = messageRetrieve;
//        this.messageProducer = messageProducer;
//    }

    @Override
    public void publish(String queueKey, Object object) throws MessageServiceException {
        try {
            messageProducer.publish(queueKey, object);
        } catch (Exception e) {
            LOGGER.error("message publish to " + queueKey + " failed", e);
            throw new MessageServiceException(e);
        }
    }

    @Override
    public Message retrieveNext(String queueKey) throws MessageServiceException {
        try {
            return messageRetrieve.retrieve(queueKey);
        } catch (Exception e) {
            LOGGER.error("message retrieve from " + queueKey + " failed", e);
            throw new MessageServiceException(e);
        }
    }

}
