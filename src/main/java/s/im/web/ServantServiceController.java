package s.im.web;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import s.im.entity.AddressInfo;
import s.im.message.MessageType;
import s.im.message.sender.server.ServerMessageSender;
import s.im.message.server.Header;
import s.im.message.server.NettyMessage;
import s.im.service.ClientChatService;
import s.im.service.mq.api.MessageQueueService;
import s.im.service.mq.api.MessageServiceException;
import s.im.util.Constant;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by za-zhujun on 2017/5/30.
 */
@RestController
@RequestMapping("/api")
public class ServantServiceController {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private ClientChatService clientChatService;
    @Autowired
    private ServerMessageSender serverMessageSender;

    @RequestMapping(value = "enqueue", method = RequestMethod.GET)
    public String addClientToServiceQueue(String userName) {
        setResponse();
        try {
            System.out.println("enqueu user: " + userName);
            messageQueueService.publish("group_1", userName);
        } catch (MessageServiceException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String hello(String userName) {
        setResponse();
        return "ok";
    }

    private void setResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");
    }


    @RequestMapping(value = "dequeue", method = RequestMethod.GET)
    public void getNextClient() {
        setResponse();
        try {
            Message message = messageQueueService.retrieveNext("group_1");
            String clientName = new String(message.getBody());
            System.out.println("dequeue user: " + clientName);
            String servantName = Constant.SERVANT_NAME;
            clientChatService.bindingChat(clientName, servantName);
        } catch (MessageServiceException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "testMessage", method = RequestMethod.GET)
    public String sendServerMessage() {
        setResponse();
        try {
            NettyMessage nettyMessage = newTestServiceMsg();
            serverMessageSender.send(new AddressInfo("127.0.0.1"), nettyMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    public static NettyMessage newTestServiceMsg() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.SERVICE_REQ.value());
        header.setMessageId(UUID.randomUUID().toString());
        message.setHeader(header);
        message.setBody("HELLO WORLD");
        return message;
    }
}
