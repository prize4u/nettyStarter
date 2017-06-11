package s.im.service;

/**
 * Created by za-zhujun on 2017/6/7.
 */
public interface ClientChatService {

    void bindingChat(String clientName, String servantName);

    void unbindingChat(String clientName, String servantName);

}
