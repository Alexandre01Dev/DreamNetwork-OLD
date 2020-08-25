package be.alexandre01.dreamzon.network.connection.spigot.commands.gui;


import be.alexandre01.dreamzon.network.connection.spigot.SpigotMain;
import be.alexandre01.dreamzon.network.connection.spigot.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryAnimation {

    public static Inventory menuAnimation(Player player, DyeColor color, String title){
        ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(byte)1);
        Inventory inv = Bukkit.createInventory(null, 36, title);
        SpigotMain.instance.inventoryManager.addInventory(player,inv);
        ItemBuilder air = new ItemBuilder(Material.AIR);
        glass.setDyeColor(color);
        glass.setName(" ");

        new BukkitRunnable() {
            int valuemm = 2;
            int oldvaluemm=1;
            int valuemin  = 3;
            int oldvaluemin=2;
            int value=4;
            int oldvalue=3;
            int valueplus = 5;
            int oldvalueplus=4;
            int valuepp = 6;
            int oldvaluepp=5;

            int bvaluemm = 29;
            int boldvaluemm=30;
            int bvaluemin  = 30;
            int boldvaluemin=31;
            int bvalue=31;
            int boldvalue=32;
            int bvalueplus = 32;
            int boldvalueplus=33;
            int bvaluepp = 33;
            int boldvaluepp=34;

            @Override
            public void run() {
                inv.setItem(setValue(oldvaluepp),air.toItemStack());
                inv.setItem(setValue(oldvalueplus),air.toItemStack());
                inv.setItem(setValue(oldvaluemm),air.toItemStack());
                inv.setItem(setValue(oldvaluemin),air.toItemStack());
                inv.setItem(setValue(oldvalue),air.toItemStack());
                inv.setItem(setValue(value),glass.toItemStack());
                inv.setItem(setValue(valuemin),glass.toItemStack());
                inv.setItem(setValue(valuemm),glass.toItemStack());
                inv.setItem(setValue(valueplus),glass.toItemStack());
                inv.setItem(setValue(valuepp),glass.toItemStack());
                oldvaluemin = setValue(oldvaluemin);
                oldvaluemm = setValue(oldvaluemm);
                oldvalueplus = setValue(oldvalueplus);
                oldvaluepp = setValue(oldvaluepp);
                oldvalue = setValue(oldvalue);
                valuemin = setValue(valuemin);
                valuemm = setValue(valuemm);
                valueplus = setValue(valueplus);
                valuepp = setValue(valuepp);
                value = setValue(value);

                inv.setItem(setValue(boldvaluepp),air.toItemStack());
                inv.setItem(setValue(boldvalueplus),air.toItemStack());
                inv.setItem(setValue(boldvaluemm),air.toItemStack());
                inv.setItem(setValue(boldvaluemin),air.toItemStack());
                inv.setItem(setValue(boldvalue),air.toItemStack());
                inv.setItem(setValue(bvalue),glass.toItemStack());
                inv.setItem(setValue(bvaluemin),glass.toItemStack());
                inv.setItem(setValue(bvaluemm),glass.toItemStack());
                inv.setItem(setValue(bvalueplus),glass.toItemStack());
                inv.setItem(setValue(bvaluepp),glass.toItemStack());
                boldvaluemin = setValue(boldvaluemin);
                boldvaluemm = setValue(boldvaluemm);
                boldvalueplus = setValue(boldvalueplus);
                boldvaluepp = setValue(boldvaluepp);
                boldvalue = setValue(boldvalue);
                bvaluemin = setValue(bvaluemin);
                bvaluemm = setValue(bvaluemm);
                bvalueplus = setValue(bvalueplus);
                bvaluepp = setValue(bvaluepp);
                bvalue = setValue(bvalue);

                if(!SpigotMain.instance.inventoryManager.isInventoryExist(inv,player)){
                    inv.clear();
                    cancel();
                }
            }
        }.runTaskTimer(SpigotMain.instance,0,2l);
        return inv;
    }
    public static int setValue(int value){
        if(value==8){
            value = 17;
            return value;
        }else {
            if(value== 17){
                value = 26;
                return value;
            }else {
                if(value== 26){
                    value = 35;
                    return value;
                } else {
                    if(value>= 0 && value < 8){
                        value++;
                        return value;
                    }else {
                        if(value<= 35 && value >= 28){
                            value--;
                            return value;
                        }else {
                            if(value== 27){
                                value = 18;
                                return value;
                            }else {
                                if(value==18){
                                    value=9;
                                    return value;
                                }else{
                                    if(value==9){
                                        value=0;
                                        return value;
                                    }
                                }
                            }


                        }
                    }
                }

            }
        }

        return value;
    }
}
