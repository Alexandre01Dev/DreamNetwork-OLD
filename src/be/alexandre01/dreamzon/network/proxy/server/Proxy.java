package be.alexandre01.dreamzon.network.proxy.server;

import be.alexandre01.dreamzon.network.client.SocketServer;
import be.alexandre01.dreamzon.network.client.communication.RequestData;
import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.enums.User;
import be.alexandre01.dreamzon.network.proxy.BungeeMain;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;
import be.alexandre01.dreamzon.network.utils.Utils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class Proxy extends Remote {
    private boolean isAuth = false;
    private User user= User.Console;
    private String name;
    private MessageChannel configChannel;
    private ChannelFuture future;
    public static Proxy proxy;
    public Proxy(ChannelFuture channelFuture){
        super(Type.Proxy);
        this.proxy = this;
        this.future = channelFuture;
        //Runnable target;
       // Thread readData = new Thread(new ReadData(this));

        //readData.start();
        configChannel = new MessageChannel("DNServerConfigurator");
        sendData(new Message().set("ALREADY",true));
        String username = "Console";
        String password = "8HetY4474XisrZ2FGwV5z";


        configChannel.setupActions(new MessageChannel.ReadChannel() {
            @Override
            public void read(Message message, MessageChannel channel) {
                System.out.println(message.getHeader());
                if(message.getHeader().equals("Identification")){
                    setProcessName(message.getString("ProcessName"));
                    System.out.println("ok -> "+ message.getString("ProcessName"));
                }
            }
        });
        System.out.println("COnstructor finished");
    }
    @EventHandler
    public void onSwitch(ServerSwitchEvent event){
        sendData(new Message().set("SWITCH",event.getPlayer().getName()));
    }

    public void readData(Message data){
        System.out.println("YES");
        System.out.println(data);
        if(isAuth){
            //for(ServerCommands remoteCommands : ServerCommands.values()){
               /* if(data.startsWith(remoteCommands.getCommand())){
                    for(Permissions permissions : remoteCommands.getPermissions()){
                        if(user.hasPermissions(permissions)){
                            System.out.println("["+ user.getUsername()+"] Perfomed Command - " + remoteCommands.getCommand());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
                            return;
                        }
                    }
                    System.out.println("["+ user.getUsername()+"] Tried Command - " + data);
                    sendData("PERM!");
                    sendData("CMD!");*/

                    if(data.contains("START")){
                        System.out.println("START");
                        String serverName = data.getString("ServerName");
                        System.out.println(serverName);
                        String ip = data.getString("IP");
                        System.out.println(ip);
                        System.out.println(data.getString("PORT"));
                        int port = Integer.parseInt(data.getString("PORT"));
                        System.out.println(port);
                        String motd = data.getString("MOTD");

                        System.out.println(motd);
                        System.out.println(data);
                        ProxyInstance.addSpigotServer(serverName,ip,port,motd);
                    }
                    if(data.contains("STOP")){
                        System.out.println("STOP! ");
                        ProxyInstance.remSpigotServer(data.getString("STOP"));
                    }
                    if (data.contains("CMD")){


                        if(data.getString("CMD").equalsIgnoreCase("stop")){
                            BungeeCord.getInstance().stop();
                            return;
                        }
                        if(data.getString("CMD").equalsIgnoreCase("reload")){
                            BungeeCord.getInstance().reloadMessages();
                            return;
                        }

                        System.out.println(data.getString("CMD"));
                        BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole(),  data.getString("CMD"));

                    }
                    if(data.contains("SLOT")){
                        try{
                            BungeeMain.instance.slot = data.getInt("SLOT");
                            BungeeMain.configuration.set("network.slot",BungeeMain.instance.slot);
                            BungeeMain.instance.saveConfig();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
            if(data.contains("MAINTENANCE")){
                try{
                    BungeeMain.instance.isMaintenance   = data.getBoolean("MAINTENANCE");
                    BungeeMain.configuration.set("network.maintenance",BungeeMain.instance.isMaintenance);
                    BungeeMain.instance.saveConfig();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if(data.contains("ADDMAINTENANCE")){
                System.out.println(data+"...");
                String playername = data.getString("ADDMAINTENANCE").toLowerCase();
                if(!BungeeMain.instance.allowedPlayer.contains(playername)){
                    BungeeMain.instance.allowedPlayer.add(playername);
                    BungeeMain.configuration.set("network.allowed-players-maintenance",BungeeMain.instance.allowedPlayer);
                    BungeeMain.instance.saveConfig();
                }
            }
            if(data.contains("REMMAINTENANCE")){
                String playername = data.getString("REMMAINTENANCE").toLowerCase();
                if(BungeeMain.instance.allowedPlayer.contains(playername)){
                    BungeeMain.instance.allowedPlayer.remove(playername);
                    BungeeMain.configuration.set("network.allowed-players-maintenance",BungeeMain.instance.allowedPlayer);
                    BungeeMain.instance.saveConfig();
                }
            }
                   if (data.contains("NAME")){
                   String name =data.getString("NAME");
                    System.out.println("Nom du processus : "+name );
              }
                    return;



        }else {
            auth(data);

        }

    }

    public void sendData(Message data){
        if(name != null){
            data.set("PROVIDERS",name);
        }

            try{
                System.out.println("send");
                RequestData msg = new RequestData();
                msg.setIntValue(123);
                msg.setMessageValue(data);
                ProxySocket.get.setChannelFuture(ProxySocket.get.getChannelFuture().channel().writeAndFlush(msg));
            }catch (Exception e){
                System.out.println("FAIL #7");
            }


        }


    public void sendData(Message data, ChannelHandlerContext ctx){
        if(name != null){
            data.set("PROVIDERS",name);
        }

        try{
            System.out.println("send");
            RequestData msg = new RequestData();
            msg.setIntValue(123);
            msg.setMessageValue(data);
            ctx.writeAndFlush(msg);
        }catch (Exception e){
            System.out.println("FAIL #7");
        }


    }
    public void setUser(User user) {
        this.user = user;
    }

    public void auth(Message data){
        try {
           /* for(Proxy remotes : Utils.remoteProxyClients){
                if(remotes.getClient().getInetAddress().equals()){
                    System.out.println("[Client] Already Connected");
                    sendData(new Message().set("ALREADY",true));
                    return;
                }
            }*/
            System.out.println("YES");
          if(data.contains("AUTH")){


            for(User user : User.values()){
                if(user.getUsername().equals(data.getString("USERNAME")) && user.getPassword().equals(data.getString("PASSWORD"))){
                    System.out.println("[Client] Successfully Auth");
                    setUser(user);
                    Utils.remoteProxyClients.add(this);
                    setAuth(true);
                    sendData(new Message().set("PROXY",true));
                    sendData(new Message().set("OK!",true));
                    return;
                }
            }

           // Utils.remoteClients.remove(this);
            setAuth(false);
            sendData(new Message().set("FAIL",true));
            System.out.println("[Client] Fail Auth " + data.getString("USERNAME") + " "+ data.getString("PASSWORD"));
            ProxySocket.get.getWorkerGroup().shutdownGracefully();
           // client.close();
        }
        }catch (Exception e){

        }
    }
    //Send Data

    public User getUser() {
        return user;
    }

    //Auth

    //getUser();


    public boolean isAuth() {
        return isAuth;
    }
    public void setAuth(boolean isAuth){
        this.isAuth = isAuth;
    }

}
