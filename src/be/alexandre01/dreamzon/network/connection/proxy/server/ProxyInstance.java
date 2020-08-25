package be.alexandre01.dreamzon.network.connection.proxy.server;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class ProxyInstance {
    public static ArrayList<String> servers = new ArrayList<>();
    public static void addSpigotServer(String processName, String ip, String port, String motd){
        System.out.println(ip);
        System.out.println(port);
        System.out.println(Integer.parseInt(port));
        try {
            ServerInfo info = ProxyServer.getInstance().constructServerInfo(processName,new InetSocketAddress(ip, Integer.parseInt(port)) , "", false);


            ProxyServer.getInstance().getServers().put(processName, info);

            servers.add(processName);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void remSpigotServer(String finalname){
        ProxyServer.getInstance().getServers().remove(finalname);
        servers.remove(finalname);
    }

}
