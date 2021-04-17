package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.objects.Server;

public class Connect {
    ThreadConnection threadConnection = null;
    Server server = null;
    public Connect(String adresse,int port,String username,String password,String processName){
        try {
            System.out.println("CONNECT>>"+port);
           threadConnection = new ThreadConnection(adresse,port,username,password,processName);
            System.out.println(processName);
           new Thread(threadConnection).start();
           if(server != null)
               threadConnection.setServer(server);
        }catch (Exception e){
            System.out.println("FAIL #1");
        }
    }

    public void setServer(Server server) {
        this.server = server;
        threadConnection.setServer(server);
    }
}

