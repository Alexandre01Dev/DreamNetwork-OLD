package be.alexandre01.dreamzon.network.spigot;

import be.alexandre01.dreamzon.network.spigot.server.Server;

import java.net.Socket;

public class WaitForConnection extends Thread{

    public void run(){
        while (!be.alexandre01.dreamzon.network.spigot.Server.getServerSocket().isClosed()){
            try {

                    Socket client = be.alexandre01.dreamzon.network.spigot.Server.getServerSocket().accept();
                if(client.getInetAddress().getHostAddress().equalsIgnoreCase("127.0.0.1")){
                    System.out.println("[" + client.getInetAddress().getHostName()+"] Client Connected => Try Auth");

                    Server remoteClass = new Server(client);
                    be.alexandre01.dreamzon.network.spigot.Server.setServer(remoteClass);
                }else {
                    System.out.println("[" +be.alexandre01.dreamzon.network.spigot.Server.getServerSocket().getInetAddress().getHostAddress()+"] Client Disconnect");
                }


            }catch (Exception e){
                System.out.println("FAIL");
            }
        }
    }
}
