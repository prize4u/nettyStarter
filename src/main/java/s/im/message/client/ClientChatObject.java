package s.im.message.client;

import java.io.Serializable;

public class ClientChatObject<T> implements Serializable {

    private String messageId;
    private byte messageType;
    private Long sendDate;
    private String userName;

    private T message;

    public ClientChatObject() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
