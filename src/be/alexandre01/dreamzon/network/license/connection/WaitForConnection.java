package be.alexandre01.dreamzon.network.license.connection;

import be.alexandre01.dreamzon.network.connection.proxy.Proxy;

import java.net.Socket;

public class WaitForConnection extends Thread{

    public void run(){
        while (!Proxy.getServer().isClosed()){
            try {

                    Socket client = Proxy.getServer().accept();

                    System.out.println("[" + client.getInetAddress().getHostName()+"] Client Connected => Try Auth");
                //    LicenseSocket  remoteClass = new LicenseSocket(client);



            }catch (Exception e){
                System.out.println("FAIL");
            }
        }
    }
}
