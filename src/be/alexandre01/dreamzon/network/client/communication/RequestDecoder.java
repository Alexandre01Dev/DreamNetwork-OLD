package be.alexandre01.dreamzon.network.client.communication;


import be.alexandre01.dreamzon.network.utils.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestData> {
    private final Charset charset = StandardCharsets.UTF_8;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode");
        RequestData data = new RequestData();
        data.setIntValue(byteBuf.readInt());
        int byteLenght = byteBuf.readInt();
        byte[] byteMessage;
        int offset;

        if (byteBuf.hasArray()) {
            byteMessage = byteBuf.array();
            offset = byteBuf.arrayOffset();
        } else {
            byteMessage = new byte[byteLenght];
            byteBuf.getBytes(byteBuf.readerIndex(), byteMessage);
            offset = 0;
        }

        data.setMessageValue(Message.createFromJsonString(new String(byteMessage)));
        list.add(data);
    }

}
