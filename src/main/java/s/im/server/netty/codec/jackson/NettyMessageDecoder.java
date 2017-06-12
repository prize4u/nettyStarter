package s.im.server.netty.codec.jackson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.InputStream;
import java.util.List;

/**
 * Created by za-zhujun on 2017/6/12.
 */
public class NettyMessageDecoder<T> extends ByteToMessageDecoder {
    private final Class<T> clazz;

    public NettyMessageDecoder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBufInputStream byteBufInputStream = new ByteBufInputStream(in);
        out.add(NettyMessageMapper.getInstance().readValue((InputStream) byteBufInputStream, clazz));

    }
}
