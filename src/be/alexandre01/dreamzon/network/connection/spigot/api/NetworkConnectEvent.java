package be.alexandre01.dreamzon.network.connection.spigot.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NetworkConnectEvent extends Event {
    private String data;
    public NetworkConnectEvent() {
    }
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}