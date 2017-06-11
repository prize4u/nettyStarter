package s.im.connection.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import s.im.connection.client.api.ClientConnectionChannel;
import s.im.connection.client.api.ClientDataHandler;
import s.im.entity.IMUser;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ChatMessageDOFactory;
import s.im.message.client.ChatObjectFactory;
import s.im.message.client.ClientChatObject;
import s.im.service.ChatMessagePersistService;
import s.im.service.ChatStateService;
import s.im.service.UserService;
import s.im.service.route.api.MessageRouteService;
import s.im.util.Constant;

/**
 * Created by za-zhujun on 2017/6/8.
 */
@Component
public class DefaultClientDataHandler implements ClientDataHandler {
    @Autowired
    private ChatMessagePersistService chatMessageService;
    @Autowired
    private MessageRouteService messageRouteService;
    @Autowired
    private ChatStateService chatStateService;
    @Autowired
    private UserService userService;

    @Override
    public void onData(ClientChatObject clientChatObject, ClientConnectionChannel clientChannel) {
        String fromUserName = clientChannel.get(Constant.LOGIN_USER_NAME);
        String chattingSessionId = clientChannel.getSessionId();
        if (userService.isServant(fromUserName)) {
            // msg persistAndSend from servant
            chattingSessionId = clientChannel.get(Constant.CHATTING_SESSION_ID);
        }

        String toUserName = clientChannel.get(Constant.CHATTING_WITH);
        if (StringUtils.isBlank(toUserName)) {
            IMUser chattingTargetUser = chatStateService.findChattingTarget(toUserName, chattingSessionId);
            toUserName = chattingTargetUser.getUserName();
        }

        // persist message from client to mongo
        ChatMessageDO chatMessageDO = ChatMessageDOFactory.newChatMessageDO(fromUserName, toUserName, chattingSessionId, clientChatObject);
        chatMessageService.insert(chatMessageDO);

        // send ack
        clientChannel.sendAckData(ChatObjectFactory.generateAckClientMessage(clientChatObject.getMessageId()));

        // route message to servant
        messageRouteService.route(chatMessageDO);
    }
}
