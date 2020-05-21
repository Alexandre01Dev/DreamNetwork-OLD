package be.alexandre01.dreamzon.network.spigot.server;

import be.alexandre01.dreamzon.network.enums.User;
import be.alexandre01.dreamzon.network.spigot.api.NetworkConnectEvent;
import be.alexandre01.dreamzon.network.spigot.api.NetworkSpigotAPI;
import be.alexandre01.dreamzon.network.spigot.api.ReadDataEvent;
import be.alexandre01.dreamzon.network.spigot.api.ReadServerDataEvent;
import be.alexandre01.dreamzon.network.utils.Crypter;
import be.alexandre01.dreamzon.network.utils.Utils;
import org.bukkit.Bukkit;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Server {
    private Socket client = null;
    private boolean isAuth = false;
    private User user= User.Console;
    private String name;

    public String getName() {
        return name;
    }
    public String getTemplateName(){
        System.out.println(name.split("-")[0]);
        return name.split("-")[0];
    }
    public Server(Socket client){
        this.client = client;


        Runnable target;
        Thread readData = new Thread(new ReadData(this));

        readData.start();
        sendData("ALREADY!");
    }
    public String getMotd(){
        try {
            return Bukkit.getMotd();
        }catch (Exception e){

            System.out.println("FAIL MOTD");
            return null;
        }

    }
    public void readData(String data){
        if(isAuth){
            System.out.println("["+getName()+"] "+data);
            Bukkit.getPluginManager().callEvent(new ReadDataEvent(data));
            if(data.contains(":")){
                String[] strings = data.split(":");
                Bukkit.getPluginManager().callEvent(new ReadServerDataEvent(strings[0],strings[1]));
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

                    if (data.startsWith("CMD")){
                        String[] splitter =data.replace("CMD","").split(";");
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
                sendData("MOTD;");
            }

            if (data.startsWith("NAME")){
                   String[] splitter =data.replace("NAME","").split(";");
                    name = splitter[1];
                  System.out.println("Nom du processus : "+name );
                Bukkit.getPluginManager().callEvent(new NetworkConnectEvent());
              }

            if(data.startsWith("ADDSERVERLIST;")){
                NetworkSpigotAPI.addServerToList(data.split(";")[1]);

            }
            if(data.startsWith("REMSERVERLIST;")){
                NetworkSpigotAPI.remServerToList(data.split(";")[1]);
            }
            if(data.startsWith("SUCCESS")){
                System.out.println("CALLEVENT");

            }
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
            for(Server remotes : Utils.remoteClients){
                if(remotes.getClient().getInetAddress().equals(client.getInetAddress())){
                    System.out.println("["+client.getInetAddress().getHostAddress()+"] Already Connected");
                    sendData("ALREADY");
                    return;
                }
            }
        String decodedKey = data;
            System.out.println("["+getName()+"]"+data);
        if(decodedKey.startsWith("#auth")){
            String[] splitter =decodedKey.replace("#auth","").split(";");
            String username = splitter[0];
            String password = splitter[1];

            for(User user : User.values()){
                if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                    System.out.println("["+client.getInetAddress().getHostAddress()+"] Successfully Auth");
                    setUser(user);
                    Utils.remoteClients.add(this);
                    setAuth(true);
                   sendData("OK!");

                   sendData("NAME;");


                    return;
                }
            }

            Utils.remoteClients.remove(this);
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
