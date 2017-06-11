package s.im.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import s.im.entity.AddressInfo;
import s.im.server.recorder.api.ClientConnectionRecorder;
import s.im.server.recorder.impl.ClientConnectionRecorderImpl;
import s.im.util.Constant;

/**
 * Created by za-zhujun on 2017/5/31.
 */
@Configuration
public class ServiceConfig {

    @Value("${socketio.starting.port}")
    private int socketIOServicingPort;

    @Bean
    public ClientConnectionRecorder clientConnectionRecorder() {
        return new ClientConnectionRecorderImpl(new AddressInfo(Constant.SELF_IP_ADDRESS, socketIOServicingPort));
    }

}
