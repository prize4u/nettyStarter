package s.im.entity.domian;

/**
 * Created by za-zhujun on 2017/5/27.
 */
public enum ChatMessageDirectionEnum {
    CLIENT_TO_SERVANT(1), SERVANT_TO_CLIENT(2);

    private int code;
    ChatMessageDirectionEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ChatMessageDirectionEnum getByCode(int code) {
        ChatMessageDirectionEnum direction = null;
        for (ChatMessageDirectionEnum direct : ChatMessageDirectionEnum.values()) {
            if (direct.getCode() == code) {
                direct = direction;
                break;
            }
        }
        return direction;
    }
}
