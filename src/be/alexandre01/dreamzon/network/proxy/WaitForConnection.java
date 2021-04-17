package be.alexandre01.dreamzon.network.proxy;

import be.alexandre01.dreamzon.network.proxy.server.ProxySocket;

import java.net.Socket;

public class WaitForConnection extends Thread{

    private String host;
    private int port;

    public WaitForConnection(String host, int port){
        this.host = host;
        this.port = port;
    }
    public void run(){

            try {

                    //Socket client = Proxy.getServer().accept();
                ProxySocket proxySocket = new ProxySocket(host,port);
                proxySocket.init();
                be.alexandre01.dreamzon.network.proxy.server.Proxy remoteClass = new be.alexandre01.dreamzon.network.proxy.server.Proxy(proxySocket.getChannelFuture());
             /*   if(client.getInetAddress().getHostAddress().equalsIgnoreCase("127.0.0.1")){
                    System.out.println("[" + client.getInetAddress().getHostName()+"] Client Connected => Try Auth");
                    be.alexandre01.dreamzon.network.proxy.server.Proxy remoteClass = new be.alexandre01.dreamzon.network.proxy.server.Proxy(client);
                }else{
                    System.out.println("\"[\" + client.getInetAddress().getHostName()+\"] Client Disconnected  => Not Authentified");
                }*/


            }catch (Exception e){
                System.out.println("FAIL");
            }
        }
}
