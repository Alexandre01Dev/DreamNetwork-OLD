package be.alexandre01.dreamzon.network.spigot.commands;

import be.alexandre01.dreamzon.network.enums.Mods;
import be.alexandre01.dreamzon.network.spigot.SpigotMain;
import be.alexandre01.dreamzon.network.spigot.api.NetworkSpigotAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class NetworkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {


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
                                sender.sendMessage("§aVous avez envoyé votre commande sur le serveur "+ args[1]);
                            }
                        }else {
                            sender.sendMessage("§e - §9/network cmd [SERVER] [COMMANDS]");
                        }

                    }
                    if(args[0].equalsIgnoreCase("gui")){
                    sender.sendMessage("§c Commande indisponnible pour le moment !");
                    }
                    if(args[0].equalsIgnoreCase("getServers")){
                        for (String server : NetworkSpigotAPI.getServers()){
                            sender.sendMessage("- "+server);
                        }
                    }
                    if(args[0].equalsIgnoreCase("slot")){
                            if(args.length >= 2){
                                Integer slot = -2;
                                try {
                                    slot = Integer.parseInt(args[1]);

                                }catch (Exception e){
                                    sender.sendMessage("§e - §c Veuillez écrire un chiffre");
                                    sender.sendMessage("§e - §9/network slot [SLOT]");
                                    return true;
                                }
                                NetworkSpigotAPI.setSlot(slot);
                                sender.sendMessage("§e - §a Tu viens de limiter le nombre de slot à "+ slot);


                            }
                    }
                        if(args[0].equalsIgnoreCase("stop")){
                            if(args.length >= 2){

                                NetworkSpigotAPI.stopServer(args[1]);
                                sender.sendMessage("§e - §a Tu viens de stopper le serveur "+ args[1]);


                            }else {
                                sender.sendMessage("§e - §9/network stop [SERVER]");
                            }
                    }

                    if(args[0].equalsIgnoreCase("getServer")){
                        sender.sendMessage("§7Current Name => §f"+NetworkSpigotAPI.getServer().getName());
                        sender.sendMessage("§7Template Name => §f"+NetworkSpigotAPI.getServer().getTemplateName());
                    }
                    if(args[0].equalsIgnoreCase("maintenance")){
                        if(args.length > 1){
                            if(args[1].equalsIgnoreCase("on")){
                                NetworkSpigotAPI.setMaintenance(true);
                                sender.sendMessage("§e- §aTu viens de démarrer le mode Maintenance.");
                            }
                            if(args[1].equalsIgnoreCase("off")){
                                NetworkSpigotAPI.setMaintenance(false);
                                sender.sendMessage("§e- §aTu viens de stopper le mode Maintenance.");
                            }
                            if(args[1].equalsIgnoreCase("add")){
                                if(args.length > 2){
                                    NetworkSpigotAPI.addPlayerFromMaintenance(args[2]);
                                    sender.sendMessage("§e- §aTu viens d'ajouter "+ args[2]+" dans la liste.");
                                }
                            }
                            if(args[1].equalsIgnoreCase("remove")){
                                if(args.length > 2){
                                    NetworkSpigotAPI.remPlayerFromMaintenance(args[2]);
                                    sender.sendMessage("§e- §aTu viens de retirer "+ args[2]+" de la liste.");
                                }
                            }
                        }else {
                            sender.sendMessage("§e- §9/network maintenance on");
                            sender.sendMessage("§e- §9/network maintenance off");
                            sender.sendMessage("§e- §9/network maintenance add <Player>");
                            sender.sendMessage("§e- §9/network maintenance remove <Player>");
                        }
                    }
                    if(args[0].equalsIgnoreCase("start")){
                        if(args.length >= 5 ){
                            if(args.length == 6){
                                NetworkSpigotAPI.startServer(args[1], Mods.valueOf(args[2].toUpperCase()),args[3],args[4],Integer.parseInt(args[5]));
                                sender.sendMessage("§e - §a Tu viens de lancer le serveur "+ args[1]);
                            }else {
                                NetworkSpigotAPI.startServer(args[1], Mods.valueOf(args[2].toUpperCase()),args[3],args[4]);
                                sender.sendMessage("§e - §a Tu viens de lancer le serveur "+ args[1]);

                            }

                        }
                    }
                    if(args[0].equalsIgnoreCase("send")){
                        if(args.length > 2){
                            try {
                                connectToServer(Bukkit.getPlayer(args[1]),args[2]);
                            }catch (Exception e){
                                sender.sendMessage("§c[§4Error§c] §e - §9Le joueur est introuvable");
                            }
                        }else {
                            sender.sendMessage("§c[§4Error§c] §e - §9/network send [Player] [Server]  §e[§6New§e]");
                        }
                    }
                }else {
                    sender.sendMessage("§6Network System 1.2 Beta:");
                    sender.sendMessage("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*");
                    sender.sendMessage("§e - §9/network gui §e[§6New§e]");
                    sender.sendMessage("§e - §9/network start [SERVER] (DYNAMIC/STATIC) (XMS) (XMX) (PORT)");
                    sender.sendMessage("§e - §9/network stop [SERVER]");
                    sender.sendMessage("§e - §9/network getServer");
                    sender.sendMessage("§e - §9/network getServers §e[§6New§e]");
                    sender.sendMessage("§e - §9/network cmd [SERVER] [COMMANDS]");
                    sender.sendMessage("§e - §9/network slot [SLOT]");
                    sender.sendMessage("§e - §9/network maintenance §e[§6New§e]");
                    sender.sendMessage("§e - §9/network send [Player] [Server]  §e[§6New§e]");
                    sender.sendMessage("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*");
                }

        }
        return false;
    }

    public void connectToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF(server);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(SpigotMain.instance, "BungeeCord", b.toByteArray());
        } catch (org.bukkit.plugin.messaging.ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
        }
    }

}
