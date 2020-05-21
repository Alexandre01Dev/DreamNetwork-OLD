package be.alexandre01.dreamzon.network.spigot;

import be.alexandre01.dreamzon.network.spigot.api.NetworkSpigotAPI;
import be.alexandre01.dreamzon.network.spigot.api.ReloadEvent;
import be.alexandre01.dreamzon.network.spigot.commands.NetworkCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class SpigotMain extends JavaPlugin implements Listener {
    private boolean playerReloading = false;
    private boolean consoleReloading = false;
    public static boolean isReloading = false;
    @Override
    public void onEnable(){
        System.out.println(Bukkit.getPort());
        Server.startServer();
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("network").setExecutor(new NetworkCommand());
    }

    @Override
    public void onDisable(){
        Server.startServer();
    }
    @EventHandler
    public void onPreProcessCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().equalsIgnoreCase("/reload")||event.getMessage().equalsIgnoreCase("/rl")){
            if(event.getPlayer().hasPermission("bukkit.command.reload")){
                if(!playerReloading|| !consoleReloading){
                    this.playerReloading = true;
                    this.consoleReloading = true;
                    ReloadEvent reloadEvent = new ReloadEvent();
                    Server.stopServer();
                    Bukkit.getServer().getPluginManager().callEvent(reloadEvent);
                    event.setCancelled(true);

                    for(Player players : Bukkit.getOnlinePlayers()){

                        if(players.hasPermission("bukkit.command.reload")){
                            players.sendMessage("§7« §fUn §eRELOAD §f est en cours §7»");
                        }
                    }


                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.reload();
                        }
                    }.runTaskLater(this,20L);
                    NetworkSpigotAPI.getServer().sendData("RELOAD");
                    isReloading = true;
                }else {
                    this.consoleReloading =  false;
                    this.playerReloading = false;
                }
            }
        }
    }
    @EventHandler
    public void onPreProcessCommand(ServerCommandEvent event){
        if(!(event.getSender() instanceof Player)){

            if(event.getCommand().equalsIgnoreCase("reload")){
                if(!playerReloading|| !consoleReloading){
                    consoleReloading = true;
                    playerReloading =true;
                    ReloadEvent reloadEvent = new ReloadEvent();
                    Bukkit.getServer().getPluginManager().callEvent(reloadEvent);
                    event.setCancelled(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.reload();
                        }
                    }.runTaskLater(this,20L);

                    for(Player players : Bukkit.getOnlinePlayers()){

                        if(players.hasPermission("bukkit.command.reload")){
                            players.sendMessage("§7« §fUn §eRELOAD §f est en cours §7»");
                        }
                    }
                    NetworkSpigotAPI.getServer().sendData("RELOAD");
                    isReloading = true;
                }else {
                    this.playerReloading = false;
                    this.consoleReloading = false;
                }

            }
        }

    }
}
