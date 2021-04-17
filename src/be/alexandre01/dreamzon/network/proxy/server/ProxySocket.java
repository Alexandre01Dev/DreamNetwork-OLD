package be.alexandre01.dreamzon.network.proxy.server;

import be.alexandre01.dreamzon.network.client.communication.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProxySocket {
    String host;
    int port;
    private ChannelFuture channelFuture;
    private    EventLoopGroup workerGroup;
    public static ProxySocket get;
    public ProxySocket(String host, int port){
        this.host = host;
        this.port = port;
        System.out.println(port);

    }

    public void init(){
        get = this;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch)
                    throws Exception {
                ch.pipeline().addLast(new RequestDataEncoder(),
                        new ResponseDataDecoder(), new ProxyHandler());
            }
        });

         channelFuture = null;
        try {
           channelFuture = b.connect(host, port).sync();
            System.out.println("YES1");
        }catch (Exception e){
            System.out.println("YES2");
            workerGroup.shutdownGracefully();
            return;
        }

        channelFuture.channel().closeFuture().addListener(future -> {
            System.out.println("ok");
            workerGroup.shutdownGracefully();
        });


        // f.channel().closeFuture().sync();

    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }
}
