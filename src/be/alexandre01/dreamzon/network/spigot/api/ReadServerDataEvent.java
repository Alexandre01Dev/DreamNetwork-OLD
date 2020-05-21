package be.alexandre01.dreamzon.network.spigot.api;

import be.alexandre01.dreamzon.network.spigot.server.Data;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;

public class ReadServerDataEvent extends Event {
    private Data data;
    private String serverName;
    public ReadServerDataEvent(String server, String data) {
        this.data = new Data(data);
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

    public void sendData(String data){
        NetworkSpigotAPI.sendDataToServer(serverName,data);
    }
}
