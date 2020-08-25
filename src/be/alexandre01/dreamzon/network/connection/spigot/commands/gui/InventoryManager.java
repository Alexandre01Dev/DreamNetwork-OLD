package be.alexandre01.dreamzon.network.connection.spigot.commands.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class InventoryManager {
    private HashMap<Player, Inventory> playerInventoryHashMap;
    public InventoryManager() {
        playerInventoryHashMap = new HashMap<>();
    }
    public Inventory getInventory(Player player){
        if(playerInventoryHashMap.containsKey(player)){
            return playerInventoryHashMap.get(player);
        }
        return null;
    }
    public void addInventory(Player player, Inventory inventory){
        playerInventoryHashMap.put(player, inventory);
    }
    public void remInventory(Player player){
        playerInventoryHashMap.remove(player);
    }
    public boolean isInventoryExist(Inventory inventory, Player player){
        if(playerInventoryHashMap.containsValue(inventory)){
            if(playerInventoryHashMap.containsKey(player)){
                return true;
            }
        }
        return false;
    }


}
