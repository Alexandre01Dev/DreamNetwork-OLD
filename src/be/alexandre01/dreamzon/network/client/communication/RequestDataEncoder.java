package be.alexandre01.dreamzon.network.client.communication;

import be.alexandre01.dreamzon.network.utils.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {
    private final Charset charset = StandardCharsets.UTF_8;
    @Override
    protected void encode(ChannelHandlerContext ctx, RequestData msg, ByteBuf out) throws Exception {
            out.writeInt(msg.getIntValue());
            String message = msg.getMessageValue().toString();
            out.writeInt(message.getBytes(StandardCharsets.UTF_8).length);
            out.writeBytes(message.getBytes(StandardCharsets.UTF_8));
    }
}
