package be.alexandre01.dreamzon.network.proxy.server;

import be.alexandre01.dreamzon.network.proxy.BungeeMain;
import be.alexandre01.dreamzon.network.spigot.WaitForConnection;
import be.alexandre01.dreamzon.network.utils.Crypter;
import be.alexandre01.dreamzon.network.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class ReadData extends Thread{
    Proxy remote;

    public ReadData(Proxy remote){
        this.remote = remote;
    }

    public void  run() {
        while (!remote.getClient().isClosed()){
            try {
                if(remote.getClient().isBound()){
                    InputStream in = remote.getClient().getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String data = null;

                    while (!remote.getClient().isClosed() && (data = reader.readLine()) != null){
                        if(Crypter.canDecode(data)){
                            remote.readData(Crypter.decode(data));
                        }

                    }
                    in.reset();
                }else {
                  System.out.println("HELLO");
                }

                if(!remote.getClient().isBound()){
                    System.out.println("HELLO!");
                }

            }catch (Exception e){

                if(remote.isAuth()){
                    System.out.println("Remote failed: Error #2");
                Utils.remoteClients.remove(this.remote);
                try{
                    remote.getClient().close();
                    this.stop();
                }catch (Exception f){
                    System.out.println("Remote failed: Error #3");
                }
                for(ProxiedPlayer player : BungeeCord.getInstance().getPlayers()){
                    player.disconnect("§cLe Network vient de se stopper. Désolé du dérangement");
                }
                BungeeCord.getInstance().stop();

                }
            }
        }
    }
    private void restartServer() {
        if (remote.getClient().isBound()) {
            try {
                remote.getClient().close();
                startServer();


            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }


    public static void startServer(){
        ServerSocket server = null;
        int port = (25565+1);

        try {
            server = new ServerSocket(port);
            be.alexandre01.dreamzon.network.proxy.Proxy.setServer(server);

            System.out.println("La communication à démarré avec succès / PORT :" + port);
            Runnable target;
            Thread waitForConnection = new Thread(new WaitForConnection());
            waitForConnection.start();

        } catch (IOException e) {
            System.out.println("La communication n'a pas pu démarré");
        }
    }
}
