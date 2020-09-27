package be.alexandre01.dreamzon.network.proxy.server;

import be.alexandre01.dreamzon.network.spigot.WaitForConnection;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;
import be.alexandre01.dreamzon.network.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
                        if(BasicCrypter.canDecode(data)){
                            Message decryptedData = Message.createFromJsonString(BasicCrypter.decode(data));
                            if(decryptedData.contains("Channel")){
                                String channelKey = decryptedData.getString("Channel");
                                MessageChannel messageChannel;
                                if(Utils.messageChannels.containsKey(channelKey)){
                                    messageChannel = Utils.messageChannels.get(channelKey);
                                }else {
                                    messageChannel = new MessageChannel(channelKey);
                                }
                                if(decryptedData.contains("DestroyChannel")){
                                    if(decryptedData.getBoolean("DestroyChannel")){
                                        messageChannel.destroy();
                                    }
                                }
                                messageChannel.receivedData(decryptedData);
                            }
                            remote.readData(decryptedData);
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
                    player.disconnect(new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\n§cLe Network vient de se stopper. Désolé du dérangement"),new TextComponent("\n\n§eVeuillez réessayer plus tard\n"),new TextComponent("§8§m*------§7§m------§7§m-§b§m-----------§7§m-§7§m------§8§m------*\nplay.octosia.fr\nNetwork System by Alexandre01"));
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
