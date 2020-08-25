package be.alexandre01.dreamzon.network.connection.spigot.api;

import be.alexandre01.dreamzon.network.enums.Mods;
import be.alexandre01.dreamzon.network.connection.spigot.SpigotServer;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;

import java.util.ArrayList;

public class NetworkSpigotAPI {
    public static ArrayList<String> servers = new ArrayList<>();
    private static ArrayList<String> templateServers = new ArrayList<>();
    public static void startServer(String name, Mods type, String Xms, String Xmx){
        MessageChannel channel = new MessageChannel();
        Message message = new Message();
        message.set("START",true);
        message.set("NAME",name);
        message.set("TYPE",type);
        message.set("XMS",Xms);
        message.set("XMX",Xmx);

        channel.sendData(message);
    }
    public static void startServer(String name){
        MessageChannel channel = new MessageChannel();
        Message message = new Message();
        channel.sendData(message.set("START",true));
    }
    public static void startServer(String name, Mods type, String Xms, String Xmx, int port){
        MessageChannel channel = new MessageChannel();
        Message message = new Message();
        message.set("START",true);
        message.set("NAME",name);
        message.set("TYPE",type);
        message.set("XMS",Xms);
        message.set("XMX",Xmx);
        message.set("PORT",port);

        channel.sendData(message);
    }
    public static void sendDataToServer(String server, Message data){
        MessageChannel channel = new MessageChannel(server);
        Message message = new Message();
        message.set("data",data);

        channel.sendData(message);

    }
    public static void stopServer(String serverName){
        MessageChannel channel = new MessageChannel(serverName);
        Message message = new Message();
        message.set("STOP",true);

        channel.sendData(message);
    }

    public static void sendCommands(String serverName, String commands){
        MessageChannel channel = new MessageChannel(serverName);
        Message message = new Message();
        message.set("CMD",commands);

        channel.sendData(message);
    }

    public static void restartServer(String name, Mods type, String Xms, String Xmx){
        MessageChannel channel = new MessageChannel(name);
        Message message = new Message();
        message.set("RESTART",true);
        message.set("NAME",name);
        message.set("TYPE",type);
        message.set("XMS",Xms);
        message.set("XMX",Xmx);

        channel.sendData(message);
    }

    public static void setMaintenance(boolean isMaintenance){
        MessageChannel channel = new MessageChannel("BungeeCord");
        channel.sendData(new Message().set("MAINTENANCE",isMaintenance));
    }
    public static void addPlayerFromMaintenance(String playerName){
        MessageChannel channel = new MessageChannel("BungeeCord");
        channel.sendData(new Message().set("ADDMAINTENANCE",playerName));
    }
    public static void remPlayerFromMaintenance(String playerName){
        MessageChannel channel = new MessageChannel("BungeeCord");
        channel.sendData(new Message().set("REMMAINTENANCE",playerName));
    }


    public static ArrayList<String> getTemplateServers() {
        return templateServers;
    }

    public static void setSlot(int slot){
        MessageChannel channel = new MessageChannel("BungeeCord");
        Message message = new Message();
        message.set("SLOT",slot);

        channel.sendData(message);
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
    public static be.alexandre01.dreamzon.network.connection.spigot.server.Server getServer(){
     return SpigotServer.getServer();
    }


}
