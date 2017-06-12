package s.im.service;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import s.im.entity.AddressInfo;
import s.im.entity.IMUser;
import s.im.service.cache.api.RedisCacheService;
import s.im.util.Constant;

import java.util.Map;

/**
 * Created by za-zhujun on 2017/5/30.
 */
@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisCacheService cacheService;
    @Value("${socketio.starting.port}")
    private int socketioConnPort;

    @Override
    public void login(IMUser user) {
        String userName = user.getUserName();

        // total login user
        Map<String, IMUser> userMap = Maps.newHashMap();
        userMap.put(user.getUserName(), user);
        cacheService.addMap(CacheConstant.LOGIN_USER_CACHE, userMap);

        if (StringUtils.equals(Constant.SERVANT_NAME, userName)) {
            // servant
            Map<String, IMUser> sessionMap = Maps.newHashMap();
            sessionMap.put(user.getUserName(), user);
            cacheService.addMap(CacheConstant.LOGIN_SERVANT_CACHE, sessionMap);
        } else {
            // client session map
            Map<String, String> sessionMap = Maps.newHashMap();
            sessionMap.put(user.getUserName(), user.getSessionId());
            cacheService.addMap(CacheConstant.LOGIN_CLIENT_SESSION_CACHE, sessionMap);
        }


    }

    @Override
    public void logout(IMUser user) {
        cacheService.deleteMapEntry(CacheConstant.LOGIN_USER_CACHE, user.getUserName());
    }

    @Override
    public void logout(String userName) {
        cacheService.deleteMapEntry(CacheConstant.LOGIN_USER_CACHE, userName);
    }

    @Override
    public AddressInfo getLoginAddress(String userName) {
        IMUser imUser = (IMUser) cacheService.getMapEntryValue(CacheConstant.LOGIN_USER_CACHE, userName);
        return imUser != null ? imUser.getLoginServer() : null;
    }

    @Override
    public boolean isUserLogin(String userName) {
        return cacheService.existEntryKey(CacheConstant.LOGIN_USER_CACHE, userName);
    }

    @Override
    public IMUser getLoginUser(String userName) {
        IMUser imUser = (IMUser) cacheService.getMapEntryValue(CacheConstant.LOGIN_USER_CACHE, userName);
        return imUser != null ? imUser : null;
    }

    @Override
    public boolean isServant(String userName) {
        return StringUtils.equals(Constant.SERVANT_NAME, userName);
    }

    @Override
    public boolean isUserOnLine(String userName) {
        return getLoginUser(userName) != null;
    }

    @Override
    public boolean isLoginOnCurrentServer(String userName) {
        boolean result = false;
        IMUser loginUser = getLoginUser(userName);
        if (loginUser != null) {
            AddressInfo loginServer = loginUser.getLoginServer();
            result = new AddressInfo(Constant.SELF_IP_ADDRESS, socketioConnPort).equals(loginServer);
        }
        return result;
    }
}
