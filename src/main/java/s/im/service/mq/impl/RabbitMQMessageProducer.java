package s.im.service.mq.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.service.mq.api.MessageProducer;

/**
 * Created by za-zhujun on 2017/5/16.
 */
@Component
public class RabbitMQMessageProducer implements MessageProducer {
    @Autowired
    private final AmqpTemplate amqpTemplate;

    public RabbitMQMessageProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }


    @Override
    public void publish(String queueKey, Object object) {
        amqpTemplate.convertAndSend(queueKey, object);
    }
}
