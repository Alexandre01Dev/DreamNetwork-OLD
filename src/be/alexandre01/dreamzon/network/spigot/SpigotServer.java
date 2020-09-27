package be.alexandre01.dreamzon.network.spigot;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.ServerSocket;

public class SpigotServer {
    private static ServerSocket serverSocket = null;
    private static int port = (Bukkit.getPort()+1);
    private static be.alexandre01.dreamzon.network.spigot.server.Server server;
    public static void startServer(){
        try {
            System.out.println(getPort());
            ServerSocket server = new ServerSocket(getPort());
            setServerSocket(server);

            System.out.println("La communication à démarré avec succès / PORT :" + getPort());
            Runnable target;
            Thread waitForConnection = new Thread(new WaitForConnection());
            waitForConnection.start();

        } catch (IOException e) {
            System.out.println("La communication n'a pas pu démarré");
        }
    }
    public static void stopServer(){
        try {
            if(getServerSocket() != null){
                getServerSocket().close();
                System.out.println("La communication à bein été stoppé avec succès / PORT :" + getPort());
            }
        } catch (IOException e) {
            System.out.println("La communication n'a pas pu se stopper");
        }
    }
    public static ServerSocket getServerSocket(){
        return serverSocket;
    }

    public static void setServerSocket(ServerSocket server){
        SpigotServer.serverSocket = server;
    }
    public static int getPort(){
        return port;
    }
    public static void setPort(int port){
        SpigotServer.port = port;
    }
    public static void setServer(be.alexandre01.dreamzon.network.spigot.server.Server server){
        SpigotServer.server = server;
    }
    public static be.alexandre01.dreamzon.network.spigot.server.Server getServer(){
        return server;
    }
}
