package s.im.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import s.im.entity.IMUser;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ChatMessageDOFactory;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.sender.client.ClientMessageSender;
import s.im.service.route.api.MessageRouteService;
import s.im.util.Constant;

/**
 * Created by za-zhujun on 2017/6/7.
 */
@Service
public class ClientChatServiceImpl implements ClientChatService {
    @Autowired
    private UserService userService;
    @Autowired
    private ClientMessageSender messageSender;
    @Autowired
    private ChatStateService chatStateService;
    @Autowired
    private ClientMessageService clientMessageService;
    @Autowired
    private MessageRouteService messageRouteService;

    @Override
    public void bindingChat(String clientName, String servantName) {
        // init chatting relationship
        IMUser clientUser = userService.getLoginUser(clientName);
        IMUser servantUser = userService.getLoginUser(servantName);
        chatStateService.registChat(clientUser, servantUser, clientUser.getSessionId());

        // persistAndSend servant greeting message
        ChatMessageDO chatMessageDO = ChatMessageDOFactory.buildS2CGreetingMessage(Constant.SERVANT_NAME, clientName);
        clientMessageService.persist(chatMessageDO);
        messageRouteService.route(chatMessageDO);
//        clientMessageService.persistAndSend(ClientEventEnum.CHAT_EVENT, chatMessageDO);
    }

    @Override
    public void unbindingChat(String clientName, String servantName) {

    }
}
