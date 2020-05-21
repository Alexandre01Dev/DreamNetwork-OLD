package be.alexandre01.dreamzon.network.spigot.server;

import be.alexandre01.dreamzon.network.spigot.SpigotMain;
import be.alexandre01.dreamzon.network.spigot.WaitForConnection;
import be.alexandre01.dreamzon.network.utils.Crypter;
import be.alexandre01.dreamzon.network.utils.Utils;
import org.apache.commons.codec.digest.Crypt;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class ReadData extends Thread{
    Server remote;

    public ReadData(Server remote){
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
                   System.out.println("hello1");
                }

                if(remote.getClient().isInputShutdown()){
                    System.out.println("hello2");
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
                    System.out.println(SpigotMain.isReloading);
                    if(!SpigotMain.isReloading)
                    Bukkit.shutdown();
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
       int port = (Bukkit.getPort()+1);

        try {
            server = new ServerSocket(port);
            be.alexandre01.dreamzon.network.spigot.Server.setServerSocket(server);

            System.out.println("La communication à démarré avec succès / PORT :" + port);
            Runnable target;
            Thread waitForConnection = new Thread(new WaitForConnection());
            waitForConnection.start();

        } catch (IOException e) {
            System.out.println("La communication n'a pas pu démarré");
        }
    }




}
