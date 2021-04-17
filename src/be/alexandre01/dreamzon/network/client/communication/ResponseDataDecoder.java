package be.alexandre01.dreamzon.network.client.communication;


import be.alexandre01.dreamzon.network.utils.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseData> {
    private final Charset charset = StandardCharsets.UTF_8;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in , List<Object> out) throws Exception {
        System.out.println("decode");
        int id = in.readInt();
        int strLen = in.readInt();
        int i = in.readInt();
        int d = in.readInt();
        int f = in.readInt();

        System.out.println("decodeID >> "+id);
        System.out.println("decodestL"+strLen);
        System.out.println(i);
        System.out.println(d);

        ResponseData data = new ResponseData();

        data.setIntValue(id);
        if(strLen != 0){
            System.out.println("Yes");
            byte[] byteMessage;
            int offset;

            if (in.hasArray()) {
                System.out.println("hmm1");
                byteMessage = in.array();
                offset = in.arrayOffset();
            } else {
                System.out.println("hmm2");
                System.out.println("hmm3");
                byteMessage = new byte[strLen];
                System.out.println("hmm4");
                System.out.println(in.copy(in.readerIndex(), byteMessage));
                offset = 0;
                System.out.println("wut ?");
            }
            System.out.println("RAH");

            System.out.println("RAH ?");
            String s = new String(byteMessage);
            data.setMessageValue(Message.createFromJsonString(new String(byteMessage)));
            System.out.println(data.getMessageValue());
            System.out.println("RAH ???");
        }
        out.add(data);
        in.clear();




    }


}
