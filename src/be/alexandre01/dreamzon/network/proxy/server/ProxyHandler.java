package be.alexandre01.dreamzon.network.proxy.server;

import be.alexandre01.dreamzon.network.client.communication.RequestData;
import be.alexandre01.dreamzon.network.client.communication.ResponseData;
import be.alexandre01.dreamzon.network.utils.message.Message;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.md_5.bungee.api.ProxyServer;

public class ProxyHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("YES :'( ");
        RequestData msg = new RequestData();
        msg.setIntValue(143);

        msg.setMessageValue(new Message().set("HOLY","FUCK"));
      //  ProxySocket.get.setChannelFuture( ctx.writeAndFlush(msg));
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Read Channel");
        System.out.println(((ResponseData)msg).getIntValue());
        ResponseData responseData = new ResponseData();
        System.out.println(Proxy.proxy);
        Proxy.proxy.readData(responseData.getMessageValue());
     //    ctx.flush();
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("flush");
      //  ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
