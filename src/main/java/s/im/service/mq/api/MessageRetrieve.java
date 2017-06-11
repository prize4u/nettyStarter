package s.im.service.mq.api;

import org.springframework.amqp.core.Message;

/**
 * 主动消费消息队列中的一条消息
 * <p>
 * Created by za-zhujun on 2017/5/16.
 */
public interface MessageRetrieve {
    Message retrieve(String queueKey);
}
