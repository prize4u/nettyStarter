package s.im.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/30.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IMUser implements Serializable {
    private String userName;
    private Date loginDate;
    private String sessionId;
    private AddressInfo loginServer;
    private AddressInfo remoteServer;

}
