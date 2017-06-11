package s.im.message.client;

import java.io.Serializable;

/**
 * Created by za-zhujun on 2017/5/27.
 */
public class ClientAckMessage implements Serializable {
    private String ackMessageId;
    private byte messageType;
    private Long sendDate;
    private String userName;

    public String getAckMessageId() {
        return ackMessageId;
    }

    public void setAckMessageId(String ackMessageId) {
        this.ackMessageId = ackMessageId;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
