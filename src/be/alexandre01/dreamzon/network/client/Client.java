package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.client.communication.ClientHandler;
import be.alexandre01.dreamzon.network.client.communication.RequestData;
import be.alexandre01.dreamzon.network.client.communication.ResponseData;
import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.enums.Mods;
import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.*;
import be.alexandre01.dreamzon.network.utils.console.colors.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

public class Client extends Remote implements IClient{
    private String username;
    private String pasword;
    private String processName;
    private String motd;
    private int port;

    public int getPort() {
        return port;
    }

    public boolean isReload = false;



    private boolean proxy = false;
    private boolean isAuth = false;
    private ChannelFuture future;
    public static Client get;
    public  Client(ChannelFuture future,String username, String password, String processName, int port) {
        super(Type.Client);
        get = this;
       this.future = future;
        this.username = username;
        this.pasword = password;
        this.processName = processName;
        this.port = port;
        Runnable target;
        setupChannels();
      //  ClientHandler.get.setRemote(this);

       // Thread readData = new Thread(new ReadData(this));
        //readData.start();

        auth();
    }
    public String getMotd(){
        return motd;
    }
    public String getProcessName() {
        return processName;
    }

    public void setupChannels(){
        MessageChannel messageChannel = new MessageChannel("DNServerConfigurator");
        messageChannel.setupActions(new MessageChannel.ReadChannel() {
            @Override
            public void read(Message message, MessageChannel channel) {
                System.out.println("Channel"+message);
                if(message.contains("STEP-1")){
                    message.getBoolean("OK");
            }
            }
        });
        MessageChannel bungeeChannel = new MessageChannel("BungeeCord");
        bungeeChannel.setupActions(new MessageChannel.ReadChannel() {
            @Override
            public void read(Message message, MessageChannel channel) {
                System.out.println("YES");
                System.out.println(message);
                if(message.contains("SLOT")){
                    Message msg = new Message();
                    msg.set("SLOT",message.getInt("SLOT"));
                    Main.getInstance().getProxy().sendData(msg);
                }
                if(message.contains("MAINTENANCE")){
                    Message msg = new Message();
                    msg.set("MAINTENANCE",message.get("MAINTENANCE"));
                    Main.getInstance().getProxy().sendData(message);
                }
                if(message.contains("ADDMAINTENANCE")){
                    Main.getInstance().getProxy().sendData(message);
                }
                if(message.contains("REMMAINTENANCE")){
                    Main.getInstance().getProxy().sendData(message);
                }
            }
        });


    }

    @Override
    public void auth(){
        Message message = new Message();
        message.set("AUTH",true);
        message.set("USERNAME",username);
        message.set("PASSWORD",pasword);
        sendData(message);
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }


    public void readData(Message data,String server,ChannelHandlerContext ctx){
        System.out.println("YES");
        System.out.println("YES*2");
        System.out.println(data);
        if (data.contains("ALREADY")) {
            Console.print("Console connecté ", Level.INFO);
            auth();
        }
        if(data.contains("FAIL")){
            Console.print("Console fail connection",Level.WARNING);
            // this.client.close();
            String pathName;
            if(isProxy()){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            ServerInstance.stopServer(processName,pathName);

        }
        if(data.contains("PROXY")){
            System.out.println(data);
            proxy = data.getBoolean("PROXY");
        }
        if(data.contains("OK!")){
            Console.print(Colors.GREEN+"Vous avez bien été connecté aux serveurs",Level.INFO);
            setAuth(true);

            sendData(new Message().set("NAME",processName),ctx);
            if(proxy){
                Main.getInstance().setProxy(this);
            }else {
               // System.out.println("GET! ici");
                sendData(new Message().set("GET",true),ctx);
            }
            Message sendServer = new Message().set("ADDSERVERLIST",getProcessName());
            sendData(sendServer);
            ArrayList<String> template = new ArrayList<>();
            for(Client s : Main.getInstance().getClients()){
                System.out.println(getProcessName()+"138");
                String sendLists = "ADDSERVERLIST;"+  getProcessName();
                template.add(s.getProcessName().split("-")[0]);

                s.sendData(new Message().set("ADDSERVERLIST",getProcessName()));
                if(!proxy){


                    System.out.println(getProcessName()+"proxy");
                    Message message = new Message();
                    message.set("START",true);
                    message.set("ServerName",processName);
                  //  message.set("IP",client.getInetAddress().getHostAddress());
                    //System.out.println(client.getPort());
                   // message.set("PORT",String.valueOf(client.getPort()-1));
                    message.set("MOTD","test");
                    Main.getInstance().getProxy().sendData(message,ctx);
                }
            }

            sendData(new Message().set("ADDTEMPLATELIST",template),ctx);

            Main.getInstance().addClient(this);
        }

        if(data.contains("START")){
        String serverName = data.getString("NAME");
        String type = data.getString("TYPE");
        String Xms = data.getString("XMS");
        String Xmx = data.getString("XMX");
        String pathName;
        if(proxy){
            pathName = "proxy";
        }else {
            pathName = "server";
        }
            Server process;
        if(data.contains("PORT")){
             process = new Server(serverName,pathName, Mods.valueOf(type),Xms,Xmx,data.getInt("PORT"),proxy);
        }else {
             process = new Server(serverName,pathName, Mods.valueOf(type),Xms,Xmx,0,proxy);
        }
            System.out.println(""+serverName+" "+ pathName+" "+ Mods.valueOf(type)+" "+Xms+" "+Xmx+" "+0+" "+proxy);
        //process.startServer();
     //   ServerInstance.startServer(serverName,pathName, Mods.valueOf(type),Xms,Xmx,0);
            process.startServer();
            //Server.startTest(pathName,serverName);
        }
        if(data.contains("STOP")){
            String serverName = data.getString("STOP").split(";")[0];
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            ServerInstance.stopServer(serverName,pathName);
        }
        if(data.contains("RESTART")){
            String[] args =data.getString("RESTART").replaceAll(" ","").split(";");
            String serverName = args[1];
            String type = args[2];
            String Xms = args[3];
            String Xmx = args[4];
            String port = args[5].replaceAll(" ","");
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            ServerInstance.stopServer(serverName,pathName);
            ServerInstance.startServer(serverName,pathName, Mods.valueOf(type),Xms,Xmx, Integer.parseInt(port));
            //ServerInstance.startServer(serverName,pathName,Type.STATIC);
        }
        if(data.contains("SENDDATA")){
          //  System.out.println("SENDDATA ! => "+ data);
            Message message = new Message();
            message.set("PROVIDER",getProcessName());
            message.set("DATA",data.get("DATA"));
            String serverName = data.getString("SENDDATA").split(";")[0];
            Client client = Main.getInstance().getClient(data.getString("SERVER"));
           // System.out.println(getProcessName()+":"+strings);
            client.sendData(message);
        }

        if(data.contains("CMD")){

            if(data.hasChannel()){
               String s = data.getString("channel");
            }
            String serverName =  data.getString("ServerName");
            String cmd =   data.getString("CMD");
            String pathName;
            System.out.println("CMD !"+cmd+" "+serverName);
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }


            Client client = Main.getInstance().getClient(serverName);
            client.sendData(data);
        }
        if(data.contains("RELOAD")){
            this.isReload = true;
            future.channel().close();

        }
        if(data.contains("TEST")){
        }
        if(data.contains("GETPROXY")){
            if(data.contains("SLOT")){
                Message message = new Message();
                message.set("SLOT",data.getInt("SLOT"));
                System.out.println("slot");
                Main.getInstance().getProxy().sendData(message);
            }
            if(data.contains("MAINTENANCE")){
                Message message = new Message();
                message.set("MAINTENANCE",data.get("MAINTENANCE"));
                Main.getInstance().getProxy().sendData(message);
            }
            if(data.contains("ADDMAINTENANCE")){
                Main.getInstance().getProxy().sendData(data);
            }
            if(data.contains("REMMAINTENANCE")){
                Main.getInstance().getProxy().sendData(data);
            }
        }
        //Console.print("["+getProcessName()+"] "+data,Level.INFO);
        if(data.contains("MOTD")){
            motd = data.getString("MOTD").split(";")[0];
           //System.out.println("SEND !");
            Message message = new Message();
            message.set("SERVERNAME",processName);
           // message.set("IP",client.getInetAddress().getHostAddress());
           // message.set("PORT",client.getPort());
            message.set("MOTD",motd);
            Main.getInstance().getProxy().sendData(message);
        }

    }
    public void sendData(Message data){
        System.out.println("send1");
            String encode = BasicCrypter.encode(data.toString());
        if(future.channel().isActive()){
            System.out.println("send2");
            try{
                ResponseData msg = new ResponseData();
                msg.setIntValue(123);

                msg.setMessageValue(data);
                System.out.println(ClientHandler.ctx);
                System.out.println(msg);
                future = ClientHandler.ctx.writeAndFlush(msg);
                System.out.println(Client.class.getSimpleName()+": "+ data);
                System.out.println("try write");
            }catch (Exception e){
                System.out.println("FAIL #7");
            }

        }else {
            Console.print("Client Connection - Closed",Level.WARNING);
        }
    }
    public void sendData(Message data, ChannelHandlerContext ctx){
        System.out.println("send1");
        String encode = BasicCrypter.encode(data.toString());
        if(future.channel().isActive()){
            System.out.println("send2");
            try{
                ResponseData msg = new ResponseData();
                msg.setIntValue(123);
                msg.setMessageValue(data);
                future = future.channel().writeAndFlush(msg);
                System.out.println(Client.class.getName()+": "+ data);
                System.out.println("try write");
            }catch (Exception e){
                System.out.println("FAIL #7");
            }

        }else {
            Console.print("Client Connection - Closed",Level.WARNING);
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return pasword;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void sendInfo(){
        Message message = new Message();
        MessageChannel messageChannel = new MessageChannel("DNServerConfigurator");
        message.setHeader("Identification");
        message.set("ProcessName",processName);
        message.set("","");
        messageChannel.sendData(message);

    }

    //ReadData

    //Auth

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }
}
