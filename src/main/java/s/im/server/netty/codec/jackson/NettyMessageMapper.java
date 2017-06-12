package s.im.server.netty.codec.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by za-zhujun on 2017/6/12.
 */
public class NettyMessageMapper {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return MAPPER;
    }
}
