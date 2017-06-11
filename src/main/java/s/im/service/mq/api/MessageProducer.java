package s.im.service.mq.api;

/**
 * 往消息队列中发送消息
 *
 * Created by za-zhujun on 2017/5/16.
 */
public interface MessageProducer {
    void publish(String queueKey, Object object);
}
