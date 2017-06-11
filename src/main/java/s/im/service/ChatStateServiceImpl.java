package s.im.service;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.entity.IMUser;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.service.cache.api.RedisCacheService;
import s.im.util.Constant;

import java.util.Map;

/**
 * Created by za-zhujun on 2017/5/30.
 */
@Component
public class ChatStateServiceImpl implements ChatStateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatStateServiceImpl.class);
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ClientConnectionRecorder clientConnectionRecorder;

    @Override
    public void registChat(IMUser clientUser, IMUser servantUser, String sessionId) {
        Map<String, IMUser> user1ChatMap = Maps.newHashMap();
        user1ChatMap.put(sessionId, servantUser);
        redisCacheService.addMap(getMapKey(clientUser), user1ChatMap);

        Map<String, IMUser> user2ChatMap = Maps.newHashMap();
        user2ChatMap.put(sessionId, clientUser);
        redisCacheService.addMap(getMapKey(servantUser), user2ChatMap);

        ClientConnectionChannel client1 = clientConnectionRecorder.findClient(clientUser.getUserName());
        if (client1 != null) {
            client1.set(Constant.CHATTING_WITH, servantUser.getUserName());
            client1.set(Constant.CHATTING_SESSION_ID, sessionId);
        }

        ClientConnectionChannel client2 = clientConnectionRecorder.findClient(servantUser.getUserName());
        if (client2 != null) {
            client2.set(Constant.CHATTING_WITH, clientUser.getUserName());
            client2.set(Constant.CHATTING_SESSION_ID, sessionId);
        }

        LOGGER.info("绑定聊天 {} <--> {},  sessionId: {}", clientUser.getUserName(), servantUser.getUserName(), sessionId);

    }

    @Override
    public void deregisChat(IMUser user, String sessionId) {
        IMUser imUser = (IMUser) redisCacheService.deleteMapEntry(getMapKey(user), sessionId);
        if (imUser != null) {
            redisCacheService.deleteMapEntry(getMapKey(imUser), sessionId);
        }
    }

    @Override
    public IMUser findChattingTarget(String userName, String sessionId) {
        IMUser imUser = (IMUser) redisCacheService.getMapEntryValue(getMapKey(userName), sessionId);
        return imUser;
    }

    private String getMapKey(IMUser user) {
        return getMapKey(user.getUserName());
    }

    private String getMapKey(String userName) {
        return CacheConstant.CHATTING_USER_PREFIX + userName;
    }


}
