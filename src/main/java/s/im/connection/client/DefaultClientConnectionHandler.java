package s.im.connection.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.connection.client.api.ClientConnectionHandler;
import s.im.entity.AddressInfo;
import s.im.entity.IMUser;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ChatMessageDOFactory;
import s.im.entity.domian.ClientEventEnum;
import s.im.server.sockio.api.IMSocketIOServer;
import s.im.service.ClientMessageService;
import s.im.service.UserService;
import s.im.util.Constant;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/6/8.
 */
@Service
public class DefaultClientConnectionHandler implements ClientConnectionHandler {
    @Autowired
    private IMSocketIOServer imSocketIOServer;
    @Autowired
    private UserService userService;
    @Autowired
    private ClientMessageService clientMessageService;
    @Value("${socketio.starting.port}")
    private int socketioConnPort;

    @Override
    public void onConnect(ClientConnectionChannel clientChannel) {
        final String userName = clientChannel.get(Constant.LOGIN_USER_NAME);

        // record client connection on current server
        imSocketIOServer.registClient(clientChannel);
        // record user login server
        IMUser newUser = newLoginUser(clientChannel);
        userService.login(newUser);

        // sending system greeting message
        ChatMessageDO chatMessageDO = ChatMessageDOFactory.buildClientGreetingMessage(userName);
        clientMessageService.persistAndSend(ClientEventEnum.CONNECT, chatMessageDO);
    }

    private IMUser newLoginUser(ClientConnectionChannel clientChannel) {
        IMUser user = new IMUser();
        user.setUserName(clientChannel.get(Constant.LOGIN_USER_NAME));
        user.setLoginDate(new Date());
        user.setLoginServer(new AddressInfo(Constant.SELF_IP_ADDRESS, socketioConnPort));
        user.setRemoteServer(clientChannel.getRemoteAddress());
        user.setSessionId(clientChannel.getSessionId());
        return user;
    }

    @Override
    public void onDisconnect(ClientConnectionChannel clientChannel) {
        final String userName = clientChannel.get(Constant.LOGIN_USER_NAME);
        userService.logout(userName);
        imSocketIOServer.deregisClient(clientChannel);
    }
}
