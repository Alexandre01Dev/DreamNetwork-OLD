package be.alexandre01.dreamzon.network.spigot.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReloadEvent extends Event {
    boolean isCancelled = false;

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
