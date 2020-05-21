package be.alexandre01.dreamzon.network.spigot.api;

import be.alexandre01.dreamzon.network.enums.Type;
import be.alexandre01.dreamzon.network.spigot.Server;

import java.util.ArrayList;

public class NetworkSpigotAPI {
    public static ArrayList<String> servers = new ArrayList<>();
    private static ArrayList<String> templateServers = new ArrayList<>();
    public static void startServer(String name, Type type, String Xms, String Xmx){
        Server.getServer().sendData("START;"+name+";"+type.name()+";"+Xms+";"+Xmx+";"+0);
    }
    public static void startServer(String name, Type type, String Xms, String Xmx,int port){
        Server.getServer().sendData("START;"+name+";"+type.name()+";"+Xms+";"+Xmx+";"+port);
    }
    public static void sendDataToServer(String server, String data){
        getServer().sendData("SENDDATA;"+server+";"+data);
    }
    public static void stopServer(String name){
        Server.getServer().sendData("STOP;"+name);
    }

    public static void sendCommands(String name, String commands){
        Server.getServer().sendData("SENDCMD;"+name+";"+commands);
    }

    public static void restartServer(String name, Type type, String Xms, String Xmx){
        Server.getServer().sendData("RESTART;"+name+";"+type.name()+";"+Xms+";"+Xmx);
    }

    public static ArrayList<String> getTemplateServers() {
        return templateServers;
    }

    public static void setSlot(int slot){
        Server.getServer().sendData("GETPROXY;SLOT;"+slot);
    }
    public static ArrayList<String> getServersList(){
        return servers;
    }
    public static void setServersList(ArrayList<String> list){
        NetworkSpigotAPI.servers = list;
    }
    public static void addServerToList(String server){
        NetworkSpigotAPI.servers.add(server);
        if(!templateServers.contains(server.split("-")[0])){
           addTemplateServerToList(server.split("-")[0]);
        }
    }
    public static void addTemplateServerToList(String server){
        NetworkSpigotAPI.templateServers.add(server);
    }
    public static ArrayList<String> getServers(){
        return servers;
    }
    public static void remServerToList(String server){
        if(NetworkSpigotAPI.servers.contains(server)){
            NetworkSpigotAPI.servers.remove(server);
        }else {
            System.out.println("ERROR ADD SERVER LIST");
        }
    }
    public static be.alexandre01.dreamzon.network.spigot.server.Server getServer(){
     return Server.getServer();
    }


}
