package be.alexandre01.dreamzon.network.license;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.remote.client.Client;
import be.alexandre01.dreamzon.network.remote.client.Connect;
import be.alexandre01.dreamzon.network.utils.Crypter;
import be.alexandre01.dreamzon.network.utils.ServerInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadData extends Thread{
    Client remote;

    public ReadData(Client remote){
        this.remote = remote;
    }

    public void  run() {
        while (!remote.getClient().isClosed()){
            try {
                InputStream in = remote.getClient().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String data = null;
                while((data = reader.readLine()) != null){
                    String decoded = Crypter.decode(data);
                    if(remote.getClient().isBound()){
                        if(decoded.contains("*")){
                            String[] splitter = decoded.split("\\*");
                            StringBuffer sb = new StringBuffer();

                            for (int i = 1; i < splitter.length; i++) {
                                sb.append(splitter[i]+" ");
                            }

                            remote.readData(sb.toString(),splitter[0]);
                        }else {
                            remote.readData(decoded,null);
                        }
                    }



                }
                System.out.println("CLOSED !");
                remote.getClient().close();
            }catch (Exception e){
                System.out.println("Remote failed: Error #2");
                try{
                    String name = remote.getProcessName();
                    remote.getClient().close();
                    String pathName;
                    if(remote.isProxy()){
                        pathName = "proxy";


                    }else {
                        pathName = "server";
                        Main.getInstance().getProxy().sendData("STOP;"+remote.getProcessName());
                        for(Client clients : Main.getInstance().getClients()){
                            String sendList = "REMSERVERLIST;"+remote.getProcessName();
                            clients.sendData(sendList);
                        }
                    }

                    if(!remote.isReload){
                        ServerInstance.stopServer(name,pathName);
                    }else{
                        System.out.println(name);
                        new Connect("localhost",remote.getPort(),"Console","8HetY4474XisrZ2FGwV5z",name);
                    }

                    this.stop();
                }catch (Exception f){
                    System.out.println("Remote failed: Error #3");
                }
            }
        }
        System.out.println("C'est down !");
        String name = remote.getProcessName();
        String pathName;
        boolean isReload = remote.isReload;
        if(remote.isProxy()){
            pathName = "proxy";


        }else {
        pathName = "server";
        pathName = "server";
        int port = remote.getPort();
        Main.getInstance().getProxy().sendData("STOP;"+remote.getProcessName());
        for(Client clients : Main.getInstance().getClients()){
            String sendList = "REMSERVERLIST;"+remote.getProcessName();
            clients.sendData(sendList);
        }
        Main.getInstance().getClients().remove(remote);
            try {
                remote.getClient().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            remote = null;

            if(!isReload){
                ServerInstance.stopServer(name,pathName);
            }else{
                System.out.println(name);
                System.out.println(port);
                new Connect("localhost",port,"Console","8HetY4474XisrZ2FGwV5z",name);
            }
    }

        this.stop();
    }
}
