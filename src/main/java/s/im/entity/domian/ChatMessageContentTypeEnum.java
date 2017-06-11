package s.im.entity.domian;

/**
 * Created by za-zhujun on 2017/5/27.
 */
public enum ChatMessageContentTypeEnum {
    TEXT(1), PIC(2), VOICE(3);

    private int code;
    ChatMessageContentTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ChatMessageContentTypeEnum getByCode(int code) {
        ChatMessageContentTypeEnum type = null;
        for (ChatMessageContentTypeEnum direct : ChatMessageContentTypeEnum.values()) {
            if (direct.getCode() == code) {
                type = direct;
                break;
            }
        }
        return type;
    }

}
