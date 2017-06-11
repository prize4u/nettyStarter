package s.im.service.mq.api;

import org.springframework.amqp.core.Message;

/**
 * 处理消息队列中收到的消息
 *
 * Created by za-zhujun on 2017/5/16.
 */
public interface MessageProcessor {
    void process(Message message) throws MessageProcessException;
}
