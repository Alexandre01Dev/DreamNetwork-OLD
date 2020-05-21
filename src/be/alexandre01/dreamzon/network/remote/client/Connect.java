package be.alexandre01.dreamzon.network.remote.client;

import be.alexandre01.dreamzon.network.spigot.WaitForConnection;
import be.alexandre01.dreamzon.network.utils.Utils;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Connect {
    public Connect(String adresse,int port,String username,String password,String processName){
        try {
            new ThreadConnection(adresse,port,username,password,processName).timer();
        }catch (Exception e){
            System.out.println("FAIL #1");
        }
    }

    }

