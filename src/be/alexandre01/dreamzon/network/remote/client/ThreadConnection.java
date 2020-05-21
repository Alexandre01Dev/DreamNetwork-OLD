package be.alexandre01.dreamzon.network.remote.client;

import be.alexandre01.dreamzon.network.utils.Utils;

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

    int i = 0;
    public ThreadConnection(String adresse,int port,String username,String password,String processName){
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
                    System.out.println("Connécté aux serveur !" + processName);

                    cancel();
                    return;
                }catch (Exception e){
                    i++;
                }
                if(i >= 159){
                    cancel();
                    return;
                }

        }

    }, 0,2000);


    }
}

