package be.alexandre01.dreamzon.network.client.communication;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseDataEncoder extends MessageToByteEncoder<ResponseData> {
    private final Charset charset = StandardCharsets.UTF_8;
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseData msg, ByteBuf out) throws Exception {
        System.out.println("encode");
        out.writeInt(msg.getIntValue());
        if(msg.getMessageValue() == null){
            System.out.println("message null");
            out.writeInt(0);
            return;
        }
        System.out.println("message pas null>>"+msg.getMessageValue().toString().length());
        System.out.println("WOW");

        String message = msg.getMessageValue().toString();
        System.out.println("hmm");
        System.out.println(""+message.getBytes(StandardCharsets.UTF_8).length);
        System.out.println("--");
        out.writeInt(message.getBytes(StandardCharsets.UTF_8).length);
        out.writeBytes(message.getBytes(StandardCharsets.UTF_8));
    }
}
