package be.alexandre01.dreamzon.network.spigot.commands.gui;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryEvent implements Listener {
    InventoryManager inventoryManager;
    public InventoryEvent(InventoryManager inventoryManager){
        this.inventoryManager = inventoryManager;
    }
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){
        if(event.getPlayer() instanceof Player){
            Player player = (Player) event.getPlayer();
            if(event.getInventory().equals(inventoryManager.getInventory(player))){
                inventoryManager.remInventory(player);
            }
        }

    }
}
