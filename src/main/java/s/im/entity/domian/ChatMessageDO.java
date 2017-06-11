package s.im.entity.domian;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/27.
 */

@Data
@NoArgsConstructor
public class ChatMessageDO implements Serializable {

    @Id
    private String id;
    private String messageId;
    private String sessionId;
    private String messageFrom;
    private String messageTo;
    private Object content;
    private int contentType;

    //
    private Date clientSendTime;
    private Date serverReceiveTime;

    // if need route between servers
    private Boolean routeNeeded = Boolean.FALSE;
    private String routeSrcHost;
    private String routeDestHost;
    private Date routeSrcSendTime;
    private Date routeDestReceiveTime;
    private Date routeDestAckTime;

    private Date messageAckTime;
    private Date gmtCreated;
    private Date gmtModified;


}
