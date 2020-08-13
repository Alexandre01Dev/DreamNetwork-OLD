package be.alexandre01.dreamzon.network.license;

import be.alexandre01.dreamzon.network.remote.client.Connect;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class License {
    public License(){
        String error = "                                                      \n" +
                "                                                                                                                                                                                 \n" +
                "██╗   ██╗ ██████╗ ██╗   ██╗██████╗     ██╗     ██╗ ██████╗███████╗███╗   ██╗███████╗███████╗    ██╗    ██╗ █████╗ ███████╗    ███████╗██╗  ██╗██████╗ ██╗██████╗ ███████╗██████╗ \n" +
                "╚██╗ ██╔╝██╔═══██╗██║   ██║██╔══██╗    ██║     ██║██╔════╝██╔════╝████╗  ██║██╔════╝██╔════╝    ██║    ██║██╔══██╗██╔════╝    ██╔════╝╚██╗██╔╝██╔══██╗██║██╔══██╗██╔════╝██╔══██╗\n" +
                " ╚████╔╝ ██║   ██║██║   ██║██████╔╝    ██║     ██║██║     █████╗  ██╔██╗ ██║███████╗█████╗      ██║ █╗ ██║███████║███████╗    █████╗   ╚███╔╝ ██████╔╝██║██████╔╝█████╗  ██║  ██║\n" +
                "  ╚██╔╝  ██║   ██║██║   ██║██╔══██╗    ██║     ██║██║     ██╔══╝  ██║╚██╗██║╚════██║██╔══╝      ██║███╗██║██╔══██║╚════██║    ██╔══╝   ██╔██╗ ██╔═══╝ ██║██╔══██╗██╔══╝  ██║  ██║\n" +
                "   ██║   ╚██████╔╝╚██████╔╝██║  ██║    ███████╗██║╚██████╗███████╗██║ ╚████║███████║███████╗    ╚███╔███╔╝██║  ██║███████║    ███████╗██╔╝ ██╗██║     ██║██║  ██║███████╗██████╔╝\n" +
                "   ╚═╝    ╚═════╝  ╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝ ╚═════╝╚══════╝╚═╝  ╚═══╝╚══════╝╚══════╝     ╚══╝╚══╝ ╚═╝  ╚═╝╚══════╝    ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚═════╝ \n" +
                "   " ;
        check();

    }

    public void check(){
    startServer();
    }
    public static void startServer(){



            //ServerSocket server = new ServerSocket(13654);
            //setServerSocket(server);

            //System.out.println("La communication à démarré avec succès / PORT :" + 18054);
        try {

            new LicenseConnect(InetAddress.getByName("protection.dreamnetwork.cloud").getHostAddress(),13654,"Console","8HetY4474XisrZ2FGwV5z","license");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //new LicenseConnect("178.63.195.240",13654,"Console","8HetY4474XisrZ2FGwV5z","license");
    }

    private static void setServerSocket(ServerSocket server) {
    }
}
