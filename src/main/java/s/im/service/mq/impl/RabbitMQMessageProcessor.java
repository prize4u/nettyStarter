package s.im.service.mq.impl;

import org.springframework.amqp.core.Message;
import s.im.service.mq.api.MessageProcessException;
import s.im.service.mq.api.MessageProcessor;

/**
 * Created by za-zhujun on 2017/5/16.
 */
public class RabbitMQMessageProcessor implements MessageProcessor {

    @Override
    public void process(Message message) throws MessageProcessException {
        // TODO: 2017/5/16 message processing
    }
}
