package be.alexandre01.dreamzon.network.connection.spigot.server;

import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.enums.User;
import be.alexandre01.dreamzon.network.connection.spigot.api.NetworkConnectEvent;
import be.alexandre01.dreamzon.network.connection.spigot.api.NetworkSpigotAPI;
import be.alexandre01.dreamzon.network.connection.spigot.api.ReadDataEvent;
import be.alexandre01.dreamzon.network.connection.spigot.api.ReadServerDataEvent;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;
import be.alexandre01.dreamzon.network.utils.Utils;
import org.bukkit.Bukkit;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Server extends Remote {
    private Socket client = null;
    private boolean isAuth = false;
    private User user= User.Console;
    private String name;
    private MessageChannel channel = new MessageChannel("DNServerConfigurator");
    public String getName() {
        return name;
    }
    public String getTemplateName(){
        System.out.println(name.split("-")[0]);
        return name.split("-")[0];
    }
    public Server(Socket client){
        super(Type.Spigot,client);
        this.client = client;

        Runnable target;
        Thread readData = new Thread(new ReadData(this));

        readData.start();
        sendData(new Message().set("ALREADY",true));
    }
    public String getMotd(){
        try {
            return Bukkit.getMotd();
        }catch (Exception e){

            System.out.println("FAIL MOTD");
            return null;
        }

    }
    public void readData(Message data){
        if(isAuth){
            System.out.println("["+getName()+"] "+data);
            Bukkit.getPluginManager().callEvent(new ReadDataEvent(data));
            if(data.contains("ServerName")){
                String serverName = data.getString("ServerName");
                Bukkit.getPluginManager().callEvent(new ReadServerDataEvent(serverName,data));
            }

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

                    if (data.contains("CMD")){
                        String[] splitter =data.getString("CMD").split(" ");
                        if(splitter[0].equalsIgnoreCase("stop")){
                            Bukkit.shutdown();
                            return;
                        }
                        if(splitter[0].equalsIgnoreCase("reload")){
                            Bukkit.shutdown();
                            return;
                        }
                        StringBuffer sb = new StringBuffer();

                        for(String s : splitter){
                            sb.append(s);
                        }

                        System.out.println(sb.toString());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),sb.toString());

                    }
            if(data.contains("GET")){
                System.out.println("GET3!!!");
               // sendData();
            }

            if (data.contains("NAME")){
                    name = data.getString("Name");
                  System.out.println("Nom du processus : "+name );
                Bukkit.getPluginManager().callEvent(new NetworkConnectEvent());
              }

            if(data.contains("ADDSERVERLIST")){
                NetworkSpigotAPI.addServerToList(data.getString("ServerName"));
               /* if(!NetworkSpigotAPI.getTemplateServers().contains(data.split(";")[1].split("-")[0])){
                    NetworkSpigotAPI.addTemplateServerToList(data.split(";")[1].split("-")[0]);
                }*/

            }
            if(data.contains("ADDTEMPLATELIST")){
                NetworkSpigotAPI.addTemplateServerToList(data.getString("ServerName"));

            }
            if(data.contains("REMSERVERLIST")){
                NetworkSpigotAPI.remServerToList(data.getString("ServerName"));
            }
            if(data.contains("SUCCESS")){
                System.out.println("CALLEVENT");
            }
        }else {
            auth(data);
        }

    }

    public void sendData(Message data){
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
            for(Server remotes : Utils.remoteClients){
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
                        Utils.remoteClients.add(this);
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
