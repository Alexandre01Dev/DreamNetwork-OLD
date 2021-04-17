package be.alexandre01.dreamzon.network.client.communication;

import be.alexandre01.dreamzon.network.client.Client;
import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.utils.Utils;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import net.minecraft.server.v1_8_R3.Tuple;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public static ChannelHandlerContext ctx = null;
    private BlockingQueue<Promise<Message>> messageList = new ArrayBlockingQueue<>(16);
    Client remote;
    public static ClientHandler get;
    public ClientHandler(){
        get  = this;
    }

    public void setRemote(Client remote) {
        System.out.println("yes remote");
        this.remote = remote;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        remote = Client.get;
        ClientHandler.ctx = ctx;
        System.out.println("Open");
        RequestData msg = new RequestData();
        msg.setIntValue(123);
        msg.setMessageValue(new Message().set("test","t"));
        ChannelFuture future = ctx.writeAndFlush(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        synchronized(this){
            Promise<Message> prom;
            while((prom = messageList.poll()) != null)
                prom.setFailure(new IOException("Connection lost"));
            messageList = null;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ID >>"+((RequestData)msg).getIntValue());
        synchronized (this){
            ClientHandler.ctx = ctx;
        Message decryptedData = ((RequestData)msg).getMessageValue();


            if(decryptedData.hasChannel()){
                System.out.println("YEs");
                String channelKey = decryptedData.getString("channel");
                System.out.println(channelKey+" wow");
                MessageChannel messageChannel;
                if(Utils.messageChannels.containsKey(channelKey)){
                    messageChannel = Utils.messageChannels.get(channelKey);
                }else {
                    messageChannel = new MessageChannel(channelKey);
                }
                if(decryptedData.contains("DestroyChannel")){
                    if(decryptedData.getBoolean("DestroyChannel")){
                        messageChannel.destroy();
                    }
                }
                messageChannel.receivedData(decryptedData);
            };

            remote.readData(  decryptedData,null,ctx);
       // ctx.flush();

        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("read complete");
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
