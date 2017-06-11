package s.im.service;

import s.im.entity.IMUser;

/**
 * Created by za-zhujun on 2017/5/30.
 */
public interface ChatStateService {

    void registChat(IMUser user1, IMUser user2, String sessionId);

    void deregisChat(IMUser user, String sessionId);

    IMUser findChattingTarget(String userName, String sessionId);




}
