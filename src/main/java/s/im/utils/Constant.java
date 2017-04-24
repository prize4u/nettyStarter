package s.im.utils;

import org.apache.tomcat.util.bcel.Const;

/**
 * Created by za-zhujun on 2017/3/28.
 */
public class Constant {
    public static final String CLIENT_CHAT_EVENT = "chatevent";
    public static final String ORG_ID = "orgId";
    public static final String THEME_ID = "themeId";
    public static final String SERVANT_IDENTIFIER = "servantID";
    public static final String SERVANT_GROUP = "servantGroup";
    public static final String SELF_IP_ADDRESS = IPUtils.getIpAddress();
    public static final Integer DEFAULT_BACKLOG = 1024;
    public static final String LOGIN_USER_NAME = "userName";
    public static final int NETTY_TIMEOUT_IN_SECONDS = 100;

    public static final int NETTY_CLIENT_RECONNECT_DELAY = 3;//3 sec
    public static final int NETTY_CLIENT_RECONNECT_TRY = 3;//3 次

    public static final int SERVER_READ_IDEL_TIME_OUT = 5; // 读超时
    public static final int SERVER_WRITE_IDEL_TIME_OUT = 0;// 写超时
    public static final int SERVER_ALL_IDEL_TIME_OUT = 0; // 所有超时

    public static final int CLIENT_READ_IDEL_TIME_OUT = 0; // 读超时
    public static final int CLIENT_WRITE_IDEL_TIME_OUT = 4;// 写超时
    public static final int CLIENT_ALL_IDEL_TIME_OUT = 0; // 所有超时
}
