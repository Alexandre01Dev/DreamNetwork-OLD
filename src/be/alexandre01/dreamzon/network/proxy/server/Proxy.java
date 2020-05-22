package be.alexandre01.dreamzon.network.proxy.server;

import be.alexandre01.dreamzon.network.enums.User;
import be.alexandre01.dreamzon.network.proxy.BungeeMain;
import be.alexandre01.dreamzon.network.utils.Crypter;
import be.alexandre01.dreamzon.network.utils.Utils;
import com.jakubson.premium.data.Messages;
import com.jakubson.premium.data.Settings;
import com.jakubson.premium.data.api.JPremiumAPI;
import com.jakubson.premium.spigot.JPremium;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Proxy {
    private Socket client = null;
    private boolean isAuth = false;
    private User user= User.Console;
    private String name;
    public Proxy(Socket client){
        this.client = client;
        System.out.println("Adresses => "+ client.getInetAddress().getHostAddress());
        if(client.getInetAddress().getHostAddress().equals("localhost")){

        }

        Runnable target;
        Thread readData = new Thread(new ReadData(this));

        readData.start();
        sendData("ALREADY!");
        String username = "Console";
        String password = "8HetY4474XisrZ2FGwV5z";

    }
    @EventHandler
    public void onSwitch(ServerSwitchEvent event){
        sendData("PLAYER;"+event.getPlayer().getServer().getInfo().getName());
    }

    public void readData(String data){
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
                    if(data.startsWith("START;")){
                        System.out.println("START! ");
                        String[] splitter = data.replace("START","").split(";");
                        System.out.println(data);
                        ProxyInstance.addSpigotServer(splitter[1],splitter[2],splitter[3],splitter[4]);
                    }
                    if(data.startsWith("STOP;")){
                        System.out.println("STOP! ");
                        String[] splitter = data.split(";");
                        System.out.println(data);
                        ProxyInstance.remSpigotServer(splitter[1]);
                    }
                    if (data.startsWith("CMD")){
                        String[] splitter =data.replace("CMD","").split(";");
                        if(splitter[0].equalsIgnoreCase("stop")){
                            BungeeCord.getInstance().stop();
                            return;
                        }
                        if(splitter[0].equalsIgnoreCase("reload")){
                            BungeeCord.getInstance().reloadMessages();
                            return;
                        }
                        StringBuffer sb = new StringBuffer();

                        for(String s : splitter){
                            sb.append(s);
                        }

                        System.out.println(sb.toString());
                        BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole(),  sb.toString());

                    }
                    if(data.startsWith("SLOT")){
                        try{
                            BungeeMain.instance.slot = Integer.parseInt(data.replaceAll("SLOT;","").replaceAll(" ",""));
                            BungeeMain.configuration.set("network.slot",BungeeMain.instance.slot);
                            BungeeMain.instance.saveConfig();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
            if(data.startsWith("MAINTENANCE;")){
                try{
                    BungeeMain.instance.isMaintenance   = Boolean.parseBoolean(data.replace("MAINTENANCE;","").replaceAll(" ",""));
                    BungeeMain.configuration.set("network.maintenance",BungeeMain.instance.isMaintenance);
                    BungeeMain.instance.saveConfig();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if(data.startsWith("ADDMAINTENANCE;")){
                System.out.println(data+"...");
                String playername = data.replace("ADDMAINTENANCE;","").replaceAll(" ","").toLowerCase();
                if(!BungeeMain.instance.allowedPlayer.contains(playername)){
                    BungeeMain.instance.allowedPlayer.add(playername);
                    BungeeMain.configuration.set("network.allowed-players-maintenance",BungeeMain.instance.allowedPlayer);
                    BungeeMain.instance.saveConfig();
                }
            }
            if(data.startsWith("REMMAINTENANCE;")){
                String playername = data.replace("REMMAINTENANCE;","").replaceAll(" ","").toLowerCase();
                if(BungeeMain.instance.allowedPlayer.contains(playername)){
                    BungeeMain.instance.allowedPlayer.remove(playername);
                    BungeeMain.configuration.set("network.allowed-players-maintenance",BungeeMain.instance.allowedPlayer);
                    BungeeMain.instance.saveConfig();
                }
            }
                   if (data.startsWith("NAME")){
                   String[] splitter =data.replace("NAME","").split(";");
                    name = splitter[1];
                  System.out.println("Nom du processus : "+name );
              }
                    return;



        }else {
            auth(Crypter.decode(data));


        }

    }

    public void sendData(String data){
        if(name != null){
            data = name+"*"+data;
        }
        data = Crypter.encode(data);

            try{
                OutputStream out = client.getOutputStream();

                PrintWriter writer = new PrintWriter(out);
                writer.write(data + "\n");
                writer.flush();
            }catch (Exception e){
                System.out.println("FAIL #7");
            }


        }



    public void setUser(User user) {
        this.user = user;
    }

    public void auth(String data){
        try {
            for(Proxy remotes : Utils.remoteProxyClients){
                if(remotes.getClient().getInetAddress().equals(client.getInetAddress())){
                    System.out.println("["+client.getInetAddress().getHostAddress()+"] Already Connected");
                    sendData("ALREADY");
                    return;
                }
            }
            System.out.println("YES");
        String decodedKey = data;
            System.out.println(data);
        if(decodedKey.startsWith("#auth")){
            String[] splitter =decodedKey.replace("#auth","").split(";");
            String username = splitter[0];
            String password = splitter[1];

            for(User user : User.values()){
                if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                    System.out.println("["+client.getInetAddress().getHostAddress()+"] Successfully Auth");
                    setUser(user);
                    Utils.remoteProxyClients.add(this);
                    setAuth(true);
                    sendData("PROXY");
                    sendData("OK!");
                    return;
                }
            }

           // Utils.remoteClients.remove(this);
            setAuth(false);
            sendData("FAIL");
            System.out.println("["+client.getInetAddress().getHostAddress()+"] Fail Auth " + username + " "+ password);
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
