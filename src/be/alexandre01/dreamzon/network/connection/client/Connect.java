package be.alexandre01.dreamzon.network.connection.client;

public class Connect {
    public Connect(String adresse,int port,String username,String password,String processName){
        try {
            new ThreadConnection(adresse,port,username,password,processName).timer();
        }catch (Exception e){
            System.out.println("FAIL #1");
        }
    }

    }

