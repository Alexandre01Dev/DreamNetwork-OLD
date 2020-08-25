package be.alexandre01.dreamzon.network.connection.proxy.server;

import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.enums.User;
import be.alexandre01.dreamzon.network.connection.proxy.BungeeMain;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;
import be.alexandre01.dreamzon.network.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Proxy extends Remote {
    private Socket client = null;
    private boolean isAuth = false;
    private User user= User.Console;
    private String name;
    private MessageChannel configChannel;
    public Proxy(Socket client){
        super(Type.Proxy,client);

        this.client = client;
        System.out.println("Adresses => "+ client.getInetAddress().getHostAddress());
        if(client.getInetAddress().getHostAddress().equals("localhost")){

        }

        Runnable target;
        Thread readData = new Thread(new ReadData(this));

        readData.start();
        configChannel = new MessageChannel("DNServerConfigurator",client);
        sendData(new Message().set("ALREADY",true));
        String username = "Console";
        String password = "8HetY4474XisrZ2FGwV5z";


        configChannel.setupActions(new MessageChannel.ReadChannel() {
            @Override
            public void read(Message message, MessageChannel channel) {
                if(message.getHeader().equals("Identification")){
                    setProcessName(message.getString("ProcessName"));

                    
                }
            }
        });
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
                    Message message = new Message();
                    message.set("BAN",BungeeMain.instance.getProxy().getPlayer("de"));

                    if(data.contains("START")){
                        System.out.println("START");
                        String serverName = data.getString("ServerName");
                        String ip = data.getString("IP");
                        String port = data.getString("PORT");
                        String motd = data.getString("MOTD");
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
            if(data.contains("ADDMAINTENANCE;")){
                System.out.println(data+"...");
                String playername = data.getString("ADDMAINTENANCE");
                if(!BungeeMain.instance.allowedPlayer.contains(playername)){
                    BungeeMain.instance.allowedPlayer.add(playername);
                    BungeeMain.configuration.set("network.allowed-players-maintenance",BungeeMain.instance.allowedPlayer);
                    BungeeMain.instance.saveConfig();
                }
            }
            if(data.contains("REMMAINTENANCE;")){
                String playername = data.getString("REMMAINTENANCE");
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
        String sData = BasicCrypter.encode(data.toString());

            try{
                OutputStream out = client.getOutputStream();

                PrintWriter writer = new PrintWriter(out);
                writer.write(sData+"\n");
                writer.flush();
            }catch (Exception e){
                System.out.println("FAIL #7");
            }


        }



    public void setUser(User user) {
        this.user = user;
    }

    public void auth(Message data){
        try {
            for(Proxy remotes : Utils.remoteProxyClients){
                if(remotes.getClient().getInetAddress().equals(client.getInetAddress())){
                    System.out.println("["+client.getInetAddress().getHostAddress()+"] Already Connected");
                    sendData(new Message().set("ALREADY",true));
                    return;
                }
            }
            System.out.println("YES");
          if(data.contains("AUTH")){


            for(User user : User.values()){
                if(user.getUsername().equals(data.getString("USERNAME")) && user.getPassword().equals(data.getString("PASSWORD"))){
                    System.out.println("["+client.getInetAddress().getHostAddress()+"] Successfully Auth");
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
            System.out.println("["+client.getInetAddress().getHostAddress()+"] Fail Auth " + data.getString("USERNAME") + " "+ data.getString("PASSWORD"));
            client.close();
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

    public Socket getClient(){
        return client;
    }
    public boolean isAuth() {
        return isAuth;
    }
    public void setAuth(boolean isAuth){
        this.isAuth = isAuth;
    }

}
