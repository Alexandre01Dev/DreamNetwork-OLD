package be.alexandre01.dreamzon.network.proxy;

import java.net.Socket;

public class WaitForConnection extends Thread{

    public void run(){
        while (!Proxy.getServer().isClosed()){
            try {

                    Socket client = Proxy.getServer().accept();
                if(client.getInetAddress().getHostAddress().equalsIgnoreCase("127.0.0.1")){
                    System.out.println("[" + client.getInetAddress().getHostName()+"] Client Connected => Try Auth");
                    be.alexandre01.dreamzon.network.proxy.server.Proxy remoteClass = new be.alexandre01.dreamzon.network.proxy.server.Proxy(client);
                }else{
                    System.out.println("\"[\" + client.getInetAddress().getHostName()+\"] Client Disconnected  => Not Authentified");
                }


            }catch (Exception e){
                System.out.println("FAIL");
            }
        }
    }
}
