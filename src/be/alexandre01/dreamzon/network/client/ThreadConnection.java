package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.Utils;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadConnection {
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
    public void timer(){

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
                try {
                    Socket socket = new Socket(adresse,port);
                    Client client = new Client(socket,username,password,processName,port-1);
                    Utils.setClient(client);
                    Console.print(Colors.ANSI_GREEN()+"Connécté au serveur " +Colors.ANSI_PURPLE+ processName);

                    cancel();
                    return;
                }catch (Exception e){
                    i++;
                }
                if(server!=null){
                    System.out.println("servernotnull");
                    if(!server.getProcessus().isAlive()){
                        cancel();
                        System.out.println("Sorry but the processus was stopped brutally");
                        return;
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

