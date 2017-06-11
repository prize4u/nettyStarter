package s.im.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import s.im.entity.domian.ChatMessageDO;

/**
 * Created by za-zhujun on 2017/5/27.
 */
@Component
public interface ChatMessageRepository extends MongoRepository<ChatMessageDO, Long> {
}
