package be.alexandre01.dreamzon.network.proxy;

        import be.alexandre01.dreamzon.network.Main;
        import be.alexandre01.dreamzon.network.proxy.server.ProxyInstance;
        import com.jakubson.premium.data.api.event.UserLoginEvent;
        import net.md_5.bungee.api.Favicon;
        import net.md_5.bungee.api.ProxyServer;
        import net.md_5.bungee.api.ServerPing;
        import net.md_5.bungee.api.chat.BaseComponent;
        import net.md_5.bungee.api.chat.TextComponent;
        import net.md_5.bungee.api.config.ServerInfo;
        import net.md_5.bungee.api.connection.ProxiedPlayer;
        import net.md_5.bungee.api.event.*;
        import net.md_5.bungee.api.plugin.Listener;
        import net.md_5.bungee.api.plugin.Plugin;
        import net.md_5.bungee.api.scheduler.ScheduledTask;
        import net.md_5.bungee.config.Configuration;
        import net.md_5.bungee.config.ConfigurationProvider;
        import net.md_5.bungee.config.YamlConfiguration;
        import net.md_5.bungee.event.EventHandler;
        import net.md_5.bungee.event.EventPriority;
        import net.tcpshield.realip.TCPShieldBungee;

        import javax.imageio.ImageIO;
        import javax.swing.*;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.URL;
        import java.util.*;
        import java.util.concurrent.TimeUnit;

public class BungeeMain  extends Plugin implements Listener {
    private Set<UUID> pending;
    public static BungeeMain instance;
    public int slot = -2;
    public static File file;
    public static Configuration configuration;
    private ArrayList<Integer> acceptedversion;
    private ScheduledTask task;
    @Override
    public void onEnable() {
        acceptedversion = new ArrayList<>();
        acceptedversion.add(47);
        acceptedversion.add(107);
        acceptedversion.add(109);
        acceptedversion.add(110);
        acceptedversion.add(210);
        acceptedversion.add(315);
        acceptedversion.add(316);
        acceptedversion.add(335);
        acceptedversion.add(338);
        acceptedversion.add(340);
        instance = this;
        pending= new HashSet<>();
        Proxy.startServer();
        loadConfig();
        getProxy().getPluginManager().registerListener(this,this);

        if(configuration.contains("network.slot")){
            slot = configuration.getInt("network.slot");
        }

        getProxy().getScheduler().schedule(this, new Runnable() {
            int i = 0;
            @Override
            public void run() {

                for(ProxiedPlayer player: getProxy().getPlayers()){
                    updateTab(player,i);
                }

                if(i == 1){
                    i = 0;
                }else {
                    i++;
                }

            }
        },0,3,TimeUnit.SECONDS);

    }
    @Override
    public void onDisable() {
        Proxy.stopServer();
    }




    private boolean isPending(UUID uuid) {
        return pending.contains(uuid);
    }
    /*@EventHandler
    public void onPlayerConnect(PostLoginEvent event) {
        pending.add(event.getPlayer().getUniqueId());
    }*/

   /* @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (! isPending(event.getPlayer().getUniqueId())) {
            return;
        }
        ProxiedPlayer player = event.getPlayer();
        ServerInfo info = getLobby();
        if (info == null) {
            return;
        }
        event.setTarget(info);
        pending.remove(player.getUniqueId());



    }*/

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        event.setCancelled(true);
        event.setCancelServer(getLobby());
    }
    @EventHandler
    public void onSwitch(ServerSwitchEvent event){

    }

    @EventHandler
    public void onJoin(ServerConnectEvent event){
        if(slot != -2){
            if(getProxy().getPlayers().size()-1>= slot){
                if(!event.getPlayer().hasPermission("network.slot.bypass")){
                    event.getPlayer().disconnect(new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\n§cServeur plein !"),new TextComponent("\n\n§eVeuillez réessayer plus tard\n"),new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\nplay.octosia.fr\nNetwork System by Alexandre01"));
                    event.setCancelled(true);
                }
            }

            updateTab(event.getPlayer(),1);
        }

    }
    public ServerInfo getLobby(){
        int i = 0;
        int max = 50;
        String word = null;
        boolean isFound = false;
        for (String str : ProxyInstance.servers){
            System.out.println(str);
            if(str.toLowerCase().startsWith("hub")){
                i = Integer.parseInt(str.split("-")[1]);
                word = str.split("-")[0];
                ServerInfo serverInfo = getProxy().getServerInfo(str);
                if(serverInfo.getPlayers().size() < 15){
                    return serverInfo;
                }

            }
        }
        if(!isFound){
            for (String str : ProxyInstance.servers){
                ServerInfo serverInfo = getProxy().getServerInfo(str);
                if(!(serverInfo.getPlayers().size() >= 50)){
                    return  serverInfo;
                }
            }
        }
        return getProxy().getServerInfo(ProxyInstance.servers.get(0));
    }
    public ServerInfo getFaction(){
        int i = 0;
        int max = 50;
        String word = null;
        boolean isFound = false;
        for (String str : ProxyInstance.servers){
            System.out.println(str);
            if(str.toLowerCase().startsWith("faction")){
                i = Integer.parseInt(str.split("-")[1]);
                word = str.split("-")[0];
                ServerInfo serverInfo = getProxy().getServerInfo(str);
                if(serverInfo.getPlayers().size() < 15){
                    return serverInfo;
                }

            }
        }
        if(!isFound){
            for (String str : ProxyInstance.servers){
                ServerInfo serverInfo = getProxy().getServerInfo(str);
                if(!(serverInfo.getPlayers().size() >= 50)){
                    return  serverInfo;
                }
            }
        }
        return getProxy().getServerInfo(ProxyInstance.servers.get(0));
    }
    public void loadConfig(){
        File theDir = new File(ProxyServer.getInstance().getPluginsFolder(), "/DreamNetwork/");
        if(!theDir.exists()){
            theDir.mkdirs();
        }

        file = new File(ProxyServer.getInstance().getPluginsFolder(), "/DreamNetwork/network.yml");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void saveConfig(){
        try{
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void updateTab(ProxiedPlayer player,int i){
        TextComponent header = new TextComponent();
        TextComponent footer = new TextComponent();
        if(i == 0){
            header.addExtra("   §6▰▰▰▰▰▰▰▰▰▰▰ §e§lOctosia §6▰▰▰▰▰▰▰▰▰▰▰");
        }else {
            header.addExtra("   §e▰▰▰▰▰▰▰▰▰▰▰ §6§lOctosia §e▰▰▰▰▰▰▰▰▰▰▰");
        }

        header.addExtra("\n\n");
        header.addExtra("§7Joueurs en ligne: "+ getProxy().getPlayers().size());
        header.addExtra("\n");
        footer.addExtra("\n");
        footer.addExtra("§2Ping : §a"+ player.getPing());
        footer.addExtra("\n\n");
        footer.addExtra("§7Site: §ewww.octosia.fr\n");
        footer.addExtra("§7Discord: §ediscord.gg/3DSHN2g");
        player.setTabHeader(header,footer);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPingTheProxy(final ProxyPingEvent e) {
        final ServerPing srvPing = e.getResponse();
        final ServerPing.Protocol version = srvPing.getVersion();
        final ServerPing.Players players = srvPing.getPlayers();


        players.setSample(new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo("§7§m-----------------", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §e §c §a §2 §5 §b §6 §9 §6Octosia", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §eEvents§7:", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §eBoss§6Event", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §cChest§4Random", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §9Spawn§3Island", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §2Uhc§aFaction", UUID.randomUUID()),new ServerPing.PlayerInfo("§7» §3Ship§bBattle", UUID.randomUUID()) ,new ServerPing.PlayerInfo("§7» §8?", UUID.randomUUID()),new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §eSite§7:", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §b§owww.octosia.fr", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §b §4", UUID.randomUUID()),new ServerPing.PlayerInfo("§7§m-----------------", UUID.randomUUID())});

        if(slot != -2 && slot <= players.getOnline()){
            srvPing.setPlayers(new ServerPing.Players(slot,players.getOnline(),players.getSample()));
        }else {
            srvPing.setPlayers(new ServerPing.Players(players.getOnline()+1,players.getOnline(),players.getSample()));
        }

        version.setName("§6Gladia§eFaction §7[§e1.8 §f-> §61.12.2§7]");
        if(!acceptedversion.contains(e.getConnection().getVersion())){
            version.setProtocol(999);
        }

        srvPing.setVersion(version);
        TextComponent component = new TextComponent();
        component.addExtra("     §7§7§l§m---§r §a§n§lOctosia§r §7§l--§r§7§l>§r §5§l[1.8 §7§l>>§5§l 1.15.2]§r §7§7§l§m---\n");
        component.addExtra("    §r§c§l«§nPvP/Faction§r§c§l»§r §e§l«§nFarmToWin§r§e§l»§r §9§l«§9Retro§9§l»");
        srvPing.setDescriptionComponent(component);
        String address = e.getConnection().getAddress().toString().substring(1).split(":")[0].replace(".","-");

        if(!ProxyInstance.servers.contains("hub-0")){
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            try {

                srvPing.setFavicon( Favicon.create(ImageIO.read(Main.class.getResourceAsStream("ressources/down.png"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }else {
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream is = classloader.getResourceAsStream("open.png");
                srvPing.setFavicon( Favicon.create(ImageIO.read(Main.class.getResourceAsStream("ressources/open.png"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        e.setResponse(srvPing);
    }

    /*@EventHandler
    public void onJoinTab(ServerConnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        TextComponent component = new TextComponent();
        component.addExtra("§8§m*------§7§m-----§7§m-§b§m-*§r§a §6Gla§edia §b§m*-§7§m-§7§m-----§8§m------*\n");
        component.addExtra("§a\n§eJoueur(s) en ligne: §b"+ProxyServer.getInstance().getPlayers()+"§7/§b"+getProxy().getPlayers().size()+1+"\n");
        component.addExtra("§7Site: §emc-gladia.fr\n");
        component.addExtra("§7Discord: §ediscord.gg/8wm9RDB\n\n");
        component.addExtra("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*");
        TextComponent component2 = new TextComponent();
        component2.addExtra("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\n\n");
        component2.addExtra("§eIp: §e§oplay.mc-gladia.fr\n\n");
        component2.addExtra("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*");

        player.setTabHeader(component,  component2);
    }*/


        // System.out.println( JPremiumAPI.getUser(player.getName()).getIp().toString());



      /*  if (config.contains("IP." + address)) {
        if(!player.getName().equals(config.getString("IP."+address+".Name"))) {
            player.disconnect(ChatUtils.format(Messages.trait + "\n" + "§cVotre pseudo ne correspond pas à votre pseudo d'origine \n\n" +
                    "§cSi cela est une erreur veuillez contacter un membre du staff\n" + Messages.trait));

            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                if (all.hasPermission("dz.staff")) {
                    all.sendMessage(ChatUtils.format(Messages.trait));
                    all.sendMessage(ChatUtils.format(Messages.doublefleche + "§b" + player.getName() + " §e à été kick pour un potentiel 'double compte'."));
                    all.sendMessage(ChatUtils.format(Messages.doublefleche + "§ePseudo d'origine: §b" + config.getString("IP." + address + ".Name")));
                    all.sendMessage(ChatUtils.format(Messages.doublefleche + "§eAdresse Ip: §b§o" + address));
                    all.sendMessage(ChatUtils.format(Messages.doublefleche + "§eStatus: §c§lKICK"));
                    all.sendMessage(ChatUtils.format(Messages.trait));
                }

                return;
            }
        }

        }else{
            player.sendMessage(ChatUtils.format(Messages.prefix+"§aVous avez été identifié par la sécurité de §9Dream§bZon §7[§cANTI-DC§7]"));

        }*/

    }








