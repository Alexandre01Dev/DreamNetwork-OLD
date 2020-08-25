package be.alexandre01.dreamzon.network.connection.spigot.api;

import be.alexandre01.dreamzon.network.connection.spigot.server.Data;
import be.alexandre01.dreamzon.network.utils.message.Message;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReadServerDataEvent extends Event {
    private Data data;
    private String serverName;
    public ReadServerDataEvent(String server, Message data) {
       // this.data = new Data(data);
        this.serverName = server;
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Data getData(){ return data; }



    public String getServerName(){ return serverName;}

    public void sendData(Message data){
        NetworkSpigotAPI.sendDataToServer(serverName,data);
    }
}
