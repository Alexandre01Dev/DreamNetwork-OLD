package be.alexandre01.dreamzon.network.connection.spigot.api;

import be.alexandre01.dreamzon.network.utils.message.Message;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReadDataEvent extends Event {
    private Message data;
    public ReadDataEvent(Message data) {
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

    public Message getData(){
        return data;
    }
}
