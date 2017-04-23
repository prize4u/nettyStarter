package s.im.message.client;

/**
 * Created by za-zhujun on 2017/3/28.
 */
public class ChatObjectFactory {

    public static ChatObject buildClientGreetingMessage() {
        ChatObject object = new ChatObject();
        object.setMessage("尊敬的客户你好，欢迎访问众安客服系统");
        object.setUserName("system");
        return object;
    }

    public static ChatObject buildServantGreetingMessage() {
        ChatObject object = new ChatObject();
        object.setMessage("你好，欢迎登陆客服系统");
        object.setUserName("system");
        return object;
    }
}
