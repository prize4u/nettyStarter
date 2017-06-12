package s.im.service;

import s.im.entity.AddressInfo;
import s.im.entity.IMUser;

/**
 * Created by za-zhujun on 2017/5/30.
 */
public interface UserService {
    void login(IMUser user);

    void logout(IMUser user);

    void logout(String userName);

    AddressInfo getLoginAddress(String userName);

    IMUser getLoginUser(String userName);

    boolean isUserLogin(String userName);

    boolean isServant(String userName);

    boolean isUserOnLine(String userName);

    boolean isLoginOnCurrentServer(String userName);
}
