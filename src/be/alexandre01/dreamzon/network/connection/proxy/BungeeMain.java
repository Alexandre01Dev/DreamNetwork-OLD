package be.alexandre01.dreamzon.network.connection.proxy;

        import be.alexandre01.dreamzon.network.Main;
        import be.alexandre01.dreamzon.network.connection.proxy.server.ProxyInstance;
        import net.md_5.bungee.api.Favicon;
        import net.md_5.bungee.api.ProxyServer;
        import net.md_5.bungee.api.ServerPing;
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

        import javax.imageio.ImageIO;
        import java.io.File;
        import java.io.IOException;
        import java.util.*;
        import java.util.concurrent.TimeUnit;

public class BungeeMain  extends Plugin implements Listener {
    private Set<UUID> pending;
    public static BungeeMain instance;
    public int slot = -2;
    public boolean isMaintenance;
    public boolean cancelKick;
    public String kickServerRedirection = null;
    public List<String> allowedPlayer;
    public static File file;
    public String lobby;
    public boolean logoStatus;
    public boolean connexionOnLobby;
    public static Configuration configuration;
    private ArrayList<Integer> acceptedversion;
    private ScheduledTask task;
    @Override
    public void onEnable() {
        allowedPlayer = new ArrayList<>();
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
        if(!configuration.contains("network.lobby")){
            configuration.set("network.lobby","hub");
            saveConfig();
        }
        lobby = configuration.getString("network.lobby");

        if(!configuration.contains("network.connexionOnLobby")){
            configuration.set("network.connexionOnLobby",true);
            saveConfig();
        }
        connexionOnLobby = configuration.getBoolean("network.connexionOnLobby");


        if(!configuration.contains("network.maintenance")){
            configuration.set("network.maintenance",false);
            saveConfig();
        }
        isMaintenance = configuration.getBoolean("network.maintenance");

        if(!configuration.contains("network.kickRedirection")){
            configuration.set("network.kickRedirection.enabled",true);
            configuration.set("network.kickRedirection.server",lobby);
            saveConfig();
        }
        cancelKick = configuration.getBoolean("network.kickRedirection.enabled");
        if(cancelKick){
            kickServerRedirection = configuration.getString("network.kickRedirection.enabled");
        }
        if(!configuration.contains("network.status")){
            configuration.set("network.status.logo",true);
            saveConfig();
        }
        logoStatus = configuration.getBoolean("network.status.logo");

        if(!configuration.contains("network.allowed-players-maintenance")){
            configuration.set("network.allowed-players-maintenance",new ArrayList<>());
            saveConfig();
        }
        for(String string : configuration.getStringList("network.allowed-players-maintenance")){
            allowedPlayer.add(string.toLowerCase());
        }

    }
    @Override
    public void onDisable() {
        Proxy.stopServer();
    }




    private boolean isPending(UUID uuid) {
        return pending.contains(uuid);
    }
    @EventHandler
    public void onPlayerConnect(PostLoginEvent event) {
        if(connexionOnLobby){
            pending.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if(connexionOnLobby){

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

        }


    }

    @EventHandler
    public void onServerKick(ServerKickEvent event) {
        if(cancelKick){
            event.setCancelled(true);
            event.setCancelServer(getServer(kickServerRedirection));
        }
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
            if(isMaintenance){
                if(!allowedPlayer.contains(event.getPlayer().getName().toLowerCase()) && !event.getPlayer().hasPermission("network.maintenance.bypass")){
                    event.getPlayer().disconnect(new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\n§cServeur en maintenance!"),new TextComponent("\n\n§eVeuillez réessayer plus tard\n"),new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\nplay.octosia.fr\nNetwork System by Alexandre01"));
                    event.setCancelled(true);
                }
            }
            updateTab(event.getPlayer(),1);
        }else {
            if(isMaintenance){
                if(!allowedPlayer.contains(event.getPlayer().getName().toLowerCase()) && !event.getPlayer().hasPermission("network.maintenance.bypass")){
                    event.getPlayer().disconnect(new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\n§cServeur en maintenance!"),new TextComponent("\n\n§eVeuillez réessayer plus tard\n"),new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\nplay.octosia.fr\nNetwork System by Alexandre01"));
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
            if(str.toLowerCase().startsWith(lobby)){
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
    public ServerInfo getServer(String server){
        int i = 0;
        int max = 50;
        String word = null;
        boolean isFound = false;
        for (String str : ProxyInstance.servers){
            System.out.println(str);
            if(str.toLowerCase().startsWith(server)){
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


        players.setSample(new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo("§7§m-----------------", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §e §c §a §2 §5 §b §6 §6Octosia", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §eEvents§7:", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §eKOTH", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §cIsland", UUID.randomUUID()), new ServerPing.PlayerInfo("§7» §9Raffinerie", UUID.randomUUID()),new ServerPing.PlayerInfo("§7» §8?", UUID.randomUUID()),new ServerPing.PlayerInfo("§7 §4 §c §e", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §eSite§7:", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §b§owww.octosia.fr", UUID.randomUUID()), new ServerPing.PlayerInfo("§7 §b §4", UUID.randomUUID()),new ServerPing.PlayerInfo("§7§m-----------------", UUID.randomUUID())});

        if(slot != -2 && slot <= players.getOnline()){
            srvPing.setPlayers(new ServerPing.Players(slot,players.getOnline(),players.getSample()));
        }else {
            srvPing.setPlayers(new ServerPing.Players(players.getOnline()+1,players.getOnline(),players.getSample()));
        }

        version.setName("§6Octosia §7[§e1.8 §f-> §61.12.2§7]");
        if(!acceptedversion.contains(e.getConnection().getVersion())){
            version.setProtocol(999);
        }

        srvPing.setVersion(version);
        TextComponent component = new TextComponent();
        component.addExtra("    §e§l✯ §6§lOctosia §e§l✯ §6Serveur §c§lPvP Faction §f§n§lFarm2Win\n");
        component.addExtra("            §e▅▆▇ §6§l150€ de §4§lCash Prize §e▇▆▅");
        srvPing.setDescriptionComponent(component);
        String address = e.getConnection().getAddress().toString().substring(1).split(":")[0].replace(".","-");
        if(logoStatus){

        if(!ProxyInstance.servers.contains(lobby+"-0")){
            try {
                srvPing.setFavicon( Favicon.create(ImageIO.read(Main.class.getResourceAsStream("ressources/down.png"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }else {
            if(isMaintenance){
                try {
                    srvPing.setFavicon( Favicon.create(ImageIO.read(Main.class.getResourceAsStream("ressources/maintenance.png"))));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else{
                try {
                    srvPing.setFavicon( Favicon.create(ImageIO.read(Main.class.getResourceAsStream("ressources/open.png"))));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

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








