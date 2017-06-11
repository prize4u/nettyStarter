package s.im.service.mq.impl;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.service.mq.api.MessageRetrieve;

/**
 * Created by za-zhujun on 2017/5/16.
 */
@Component
public class RabbitMQMessageConsumer implements MessageRetrieve {
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQMessageConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Message retrieve(String queueKey) {
        return rabbitTemplate.receive(queueKey);
    }
}
