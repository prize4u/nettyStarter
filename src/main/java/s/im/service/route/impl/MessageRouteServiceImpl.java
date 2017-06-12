package s.im.service.route.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import s.im.entity.AddressInfo;
import s.im.entity.domian.ChatMessageDO;
import s.im.entity.domian.ClientEventEnum;
import s.im.message.sender.client.ClientMessageSender;
import s.im.message.sender.server.ServerMessageSender;
import s.im.message.server.NettyMessage;
import s.im.message.server.NettyMessageFactory;
import s.im.service.ChatMessagePersistService;
import s.im.service.ClientMessageService;
import s.im.service.UserService;
import s.im.service.route.api.MessageRouteService;
import s.im.util.Constant;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by za-zhujun on 2017/5/30.
 */
@Service
public class MessageRouteServiceImpl implements MessageRouteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouteServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ServerMessageSender serverMessageSender;
    @Autowired
    private ClientMessageService clientMessageService;
    @Autowired
    private ChatMessagePersistService messagePersistService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public void route(ChatMessageDO chatMessageDO) {
        executorService.submit(new RouteMessageThread(chatMessageDO));
    }


    private class RouteMessageThread implements Runnable {
        private ChatMessageDO chatMessageDO;

        RouteMessageThread(ChatMessageDO chatMessageDO) {
            this.chatMessageDO = chatMessageDO;
        }

        @Override
        public void run() {
            try {
                String messageTo = chatMessageDO.getMessageTo();
                if (userService.isUserOnLine(messageTo)) {
                    if (userService.isLoginOnCurrentServer(messageTo)) {
                        // sender and receiver login in same server
                        clientMessageService.sendDirectly(ClientEventEnum.CHAT_EVENT, chatMessageDO);
                    } else {
                        AddressInfo senderLoginAddress = userService.getLoginAddress(chatMessageDO.getMessageFrom());
                        AddressInfo receiverLoginAddress = userService.getLoginAddress(messageTo);
                        // in different server, persistAndSend to target server by netty, need route
                        messagePersistService.updateRouteSendTime(chatMessageDO.getMessageId()
                                , senderLoginAddress, receiverLoginAddress, new Date());

                        NettyMessage nettyMessage = NettyMessageFactory.clientMessageToNettyMessage(chatMessageDO);
                        LOGGER.info("{} 登录在 {}， {} 登录在 {}， 需要netty路由 {}"
                                , chatMessageDO.getMessageFrom(), senderLoginAddress
                                , messageTo, receiverLoginAddress
                                , nettyMessage);
                        serverMessageSender.send(receiverLoginAddress, nettyMessage);
                    }
                } else {
                    LOGGER.warn("{} is not on line", messageTo);
                }
            } catch (Exception e) {
                LOGGER.error("error route message", e);
            }

        }
    }
}
