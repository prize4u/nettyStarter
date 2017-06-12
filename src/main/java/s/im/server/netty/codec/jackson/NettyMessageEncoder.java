package s.im.server.netty.codec.jackson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import s.im.message.server.NettyMessage;

import java.io.OutputStream;

/**
 * Created by za-zhujun on 2017/6/12.
 */
public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(out);
        NettyMessageMapper.getInstance().writeValue((OutputStream) byteBufOutputStream, msg);
    }
}
