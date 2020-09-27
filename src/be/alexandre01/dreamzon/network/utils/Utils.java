package be.alexandre01.dreamzon.network.utils;

import be.alexandre01.dreamzon.network.proxy.server.Proxy;
import be.alexandre01.dreamzon.network.client.Client;
import be.alexandre01.dreamzon.network.spigot.server.Server;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Utils {
    public static ArrayList<String> templates = new ArrayList<>();
    public static LinkedList<Server> remoteClients = new LinkedList<>();
    public static LinkedList<Proxy> remoteProxyClients = new LinkedList<>();
    private static Client client = null;
    public static boolean isMultiThread = true;
    public static Socket socket = null;
    public static HashMap<String, MessageChannel> messageChannels = new HashMap<>();
    public static void setClient(Client client) {
        Utils.client = client;
    }

    public static Client getClient() {
        return client;
    }
}

