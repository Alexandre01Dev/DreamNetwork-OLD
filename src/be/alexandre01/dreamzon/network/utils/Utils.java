package be.alexandre01.dreamzon.network.utils;

import be.alexandre01.dreamzon.network.proxy.server.Proxy;
import be.alexandre01.dreamzon.network.remote.client.Client;
import be.alexandre01.dreamzon.network.spigot.server.Server;

import java.util.LinkedList;

public class Utils {
    public static LinkedList<Server> remoteClients = new LinkedList<>();
    public static LinkedList<Proxy> remoteProxyClients = new LinkedList<>();
    private static Client client = null;

    public static void setClient(Client client) {
        Utils.client = client;
    }

    public static Client getClient() {
        return client;
    }
}

