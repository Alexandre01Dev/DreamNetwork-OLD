package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.console.colors.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.Utils;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadConnection extends Thread{
    boolean isFound;
    String adresse;
    int port;
    String username;
    String password;
    String processName;
    Server server = null;
    int i = 0;
    public ThreadConnection(String adresse, int port, String username, String password, String processName){
        this.adresse = adresse;
        this.port = port;
        this.username = username;
        this.password = password;
        this.processName = processName;
    }
    @Override
    public void run(){

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
                try {
                    try {
                        System.out.println("thread");
                        SocketServer socketServer = new SocketServer(username,password,processName,adresse,port);
                        socketServer.init();
                        System.out.println("init");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // Socket socket = new Socket(adresse,port);
                   // Client client = new Client(socket,username,password,processName,port-1);
                  //  Utils.setClient(client);
                    Console.print(Colors.ANSI_GREEN()+"Connécté au serveur " +Colors.ANSI_PURPLE+ processName);
                    cancel();
                    return;
                }catch (Exception e){
                    //e.printStackTrace();
                    i++;
                }
                if(server!=null){
                //    System.out.println("servernotnull");
                    if(!server.getProcessus().isAlive()){
                        //cancel();
                        System.out.println("Sorry but the processus was stopped brutally");
                        //return;
                    }
                }
                if(i >= 159){
                    cancel();
                    return;
                }

        }

    }, 0,2000);


    }

    public void setServer(Server server) {
        this.server = server;
    }
}

