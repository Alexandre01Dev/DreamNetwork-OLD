package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.client.communication.ClientHandler;
import be.alexandre01.dreamzon.network.client.communication.RequestDecoder;
import be.alexandre01.dreamzon.network.client.communication.ResponseDataEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.Socket;

public class SocketServer {
    private String address;
    private String username;
    private String password;
    private String process;
    private int port;
    private ChannelFuture channelFuture;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    public static SocketServer get;
    public SocketServer(String username,String password,String process,String address, int port) throws Exception {

        this.username = username;
        this.password = password;
        this.process = process;
        this.port = port;
       this.address = address;
       init();
    }

    public void init() throws Exception{
        get = this;
         bossGroup = new NioEventLoopGroup();
         workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap b = new ServerBootstrap()

                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new RequestDecoder(),
                                    new ResponseDataEncoder(),new ClientHandler()
                                    );
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    ;

            //START

            

             channelFuture = null;
            channelFuture = b.bind(address, port).sync().addListener(future -> {
                Client client = new Client(channelFuture,username,password,process,port-1);
//                 ClientHandler.get.setRemote(client);
                System.out.println("DONE");
            });

            System.out.println("Started socket on : "+address+":"+port);
            // Wait
            try {
                channelFuture.channel().closeFuture().sync().addListener(future -> {
                    System.out.println("close future");
                });
            } catch (Exception e) {
                System.out.println("Socket closed");
            }
        } finally {
            //Shutdown
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }
}
