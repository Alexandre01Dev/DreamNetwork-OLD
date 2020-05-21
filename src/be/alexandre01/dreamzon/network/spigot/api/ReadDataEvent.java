package be.alexandre01.dreamzon.network.spigot.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReadDataEvent extends Event {
    private String data;
    public ReadDataEvent(String data) {
        this.data = data;
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getData(){
        return data;
    }
}
