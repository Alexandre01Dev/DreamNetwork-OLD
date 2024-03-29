package be.alexandre01.dreamzon.network.proxy;



import java.io.IOException;
import java.net.ServerSocket;

public class Proxy {

    private static ServerSocket server = null;
    private static int port = (25565 +1);

    public static void startServer(){
        // ServerSocket server = new ServerSocket(getPort());
        //setServer(server);

        System.out.println("La communication à démarré avec succès / PORT :" + getPort());
        Runnable target;
        Thread waitForConnection = new Thread(new WaitForConnection("localhost",port));
        waitForConnection.start();

    }
    public static void stopServer(){
        try {
            if(getServer() != null){
                getServer().close();
                System.out.println("La communication à bien été stoppé avec succès / PORT :" + getPort());
            }
        } catch (IOException e) {
            System.out.println("La communication n'a pas pu se stopper");
        }
    }
    public static ServerSocket getServer(){
        return server;
    }
    public static void setServer(ServerSocket server){
        Proxy.server = server;
    }
    public static int getPort(){
        return port;
    }
    public static void setPort(int port){
        Proxy.port = port;
    }
}
