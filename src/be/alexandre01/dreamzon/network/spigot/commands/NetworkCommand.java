package be.alexandre01.dreamzon.network.spigot.commands;

import be.alexandre01.dreamzon.network.enums.Type;
import be.alexandre01.dreamzon.network.spigot.api.NetworkSpigotAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NetworkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("network")){
                if(args.length >= 1){
                    if(args[0].equalsIgnoreCase("cmd")){
                        if(args.length >= 3){
                            if(NetworkSpigotAPI.getServersList().contains(args[1])){
                                StringBuffer sb = new StringBuffer();

                                for (int i = 2; i < args.length; i++) {
                                    if(i == args.length){
                                        sb.append(args[i]);
                                    }else {
                                        sb.append(args[i]+" ");
                                    }

                                }

                                NetworkSpigotAPI.sendCommands(args[1],sb.toString());
                                player.sendMessage("§aVous avez envoyé votre commande sur le serveur "+ args[1]);
                            }
                        }else {
                            player.sendMessage("§e - §9/network cmd [SERVER] [COMMANDS]");
                        }

                    }
                    if(args[0].equalsIgnoreCase("slot")){
                            if(args.length >= 2){
                                Integer slot = -2;
                                try {
                                    slot = Integer.parseInt(args[1]);

                                }catch (Exception e){
                                    player.sendMessage("§e - §c Veuillez écrire un chiffre");
                                    player.sendMessage("§e - §9/network slot [SLOT]");
                                    return true;
                                }
                                NetworkSpigotAPI.setSlot(slot);
                                player.sendMessage("§e - §a Tu viens de limiter le nombre de slot à "+ slot);


                            }
                    }
                        if(args[0].equalsIgnoreCase("stop")){
                            if(args.length >= 2){

                                NetworkSpigotAPI.stopServer(args[1]);
                                player.sendMessage("§e - §a Tu viens de stopper le serveur "+ args[1]);


                            }else {
                                player.sendMessage("§e - §9/network stop [SERVER]");
                            }
                    }

                    if(args[0].equalsIgnoreCase("getServer")){
                        player.sendMessage("§7Current Name => §f"+NetworkSpigotAPI.getServer().getName());
                        player.sendMessage("§7Template Name => §f"+NetworkSpigotAPI.getServer().getTemplateName());
                    }

                    if(args[0].equalsIgnoreCase("start")){
                        if(args.length >= 5 ){
                            if(args.length == 6){
                                NetworkSpigotAPI.startServer(args[1], Type.valueOf(args[2].toUpperCase()),args[3],args[4],Integer.parseInt(args[5]));
                            player.sendMessage("§e - §a Tu viens de lancer le serveur "+ args[1]);
                        }else {
                                NetworkSpigotAPI.startServer(args[1], Type.valueOf(args[2].toUpperCase()),args[3],args[4]);
                                player.sendMessage("§e - §a Tu viens de lancer le serveur "+ args[1]);

                            }

                        }
                    }
                }else {
                    player.sendMessage("§e - §9/network start [SERVER] [DYNAMIC/STATIC] [XMS] [XMX] (PORT)");
                    player.sendMessage("§e - §9/network stop [SERVER]");
                    player.sendMessage("§e - §9/network getServer");
                    player.sendMessage("§e - §9/network cmd [SERVER] [COMMANDS]");
                    player.sendMessage("§e - §9/network slot [SLOT]");
                }
            }
        }
        return false;
    }
}
