import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import s.im.Application;
import s.im.dao.ChatMessageDAO;
import s.im.dao.ChatMessageRepository;
import s.im.entity.domian.ChatMessageContentTypeEnum;
import s.im.entity.domian.ChatMessageDirectionEnum;
import s.im.entity.domian.ChatMessageDO;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageDAO chatMessageDAO;

    @Test
    public void testInsert() {
        ChatMessageDO chatMessageDO = new ChatMessageDO();
        chatMessageDO.setMessageId("XX-ABC");
//        chatMessageDO.setClientId("clientid");
//        chatMessageDO.setServantId("servantId");
        chatMessageDO.setContent("message content");
        chatMessageDO.setContentType(ChatMessageContentTypeEnum.TEXT.getCode());
//        chatMessageDO.setServerAckTime(new Date());
//        chatMessageDO.setMessageDirection(ChatMessageDirectionEnum.CLIENT_TO_SERVANT.getCode());
//        chatMessageDO.setSendTime(new Date());
        chatMessageDO.setSessionId("sessionId");
        chatMessageDO.setGmtCreated(new Date());
        chatMessageDO.setGmtModified(new Date());
        ChatMessageDO insert = chatMessageDAO.insert(chatMessageDO);
    }

    @Test
    public void testFind() {
        ChatMessageDO chatMessageDO = chatMessageDAO.find("XX-ABC");
//        System.out.println(chatMessageDO.getClientId());
    }

    @Test
    public void testUpdate() {
//        chatMessageDAO.updateAckTime("XX-ABC", new Date());
//        ChatMessageDO chatMessageDO = chatMessageDAO.find("XX-ABC");
//        System.out.println(chatMessageDO.getAckTime());
    }
}
