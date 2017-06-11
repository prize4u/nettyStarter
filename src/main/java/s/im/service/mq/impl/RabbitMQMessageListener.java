package s.im.service.mq.impl;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * 监听消息队列
 *
 * Created by za-zhujun on 2017/5/16.
 */
public class RabbitMQMessageListener implements MessageListener {
    @Override
    public void onMessage(Message msg) {

    }
}
