package be.alexandre01.dreamzon.network.connection.spigot.commands.gui;

import be.alexandre01.dreamzon.network.connection.spigot.api.NetworkSpigotAPI;
import be.alexandre01.dreamzon.network.connection.spigot.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NetworkGui {
    public NetworkGui(Player player){
        Inventory inv = InventoryAnimation.menuAnimation(player, DyeColor.BLUE,"§e- §9Network System [Menu]");
        int page = 1;
        ItemBuilder itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE);
        int slot = 11;
        for (String template : NetworkSpigotAPI.getTemplateServers()){
            for(String servers : NetworkSpigotAPI.getServers()){
                boolean b = true;
                if(servers.split("-")[0].equals(template)){
                    itemBuilder.setDyeColor(DyeColor.GREEN);
                    itemBuilder.setName("§e-§a"+template);
                    b = false;
                    break;
                }
                if(b){
                    itemBuilder.setName("§e-§a"+template);
                    itemBuilder.setDyeColor(DyeColor.GRAY);
                }

            }
            inv.setItem(slot,itemBuilder.toItemStack());
        slot++;
            if(slot == 25){
                ItemBuilder pageItem = new ItemBuilder(Material.ARROW);
                pageItem.setName("PAGE "+page);
                inv.setItem(25,pageItem.toItemStack());
                slot = 12;
            }
        }
    }
}
