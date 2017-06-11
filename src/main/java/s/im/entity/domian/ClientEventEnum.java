package s.im.entity.domian;

/**
 * Created by za-zhujun on 2017/6/1.
 */
public enum ClientEventEnum {
    CONNECT("connect"), DISCONNECT("disconnect"), CHAT_EVENT("chatevent");

    private String code;
    ClientEventEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ClientEventEnum getByCode(String code) {
        ClientEventEnum direction = null;
        for (ClientEventEnum direct : ClientEventEnum.values()) {
            if (direct.getCode() == code) {
                direct = direction;
                break;
            }
        }
        return direction;
    }
}
