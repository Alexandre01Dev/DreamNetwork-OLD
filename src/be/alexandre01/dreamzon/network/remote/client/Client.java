package be.alexandre01.dreamzon.network.remote.client;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.enums.Type;
import be.alexandre01.dreamzon.network.proxy.server.ProxyInstance;
import be.alexandre01.dreamzon.network.spigot.Server;
import be.alexandre01.dreamzon.network.utils.Crypter;
import be.alexandre01.dreamzon.network.utils.ServerInstance;
import be.alexandre01.dreamzon.network.utils.Utils;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    private Socket client;
    private String username;
    private String pasword;
    private String processName;
    private String motd;
    private int port;

    public int getPort() {
        return port;
    }

    public boolean isReload = false;



    private boolean proxy = false;
    private boolean isAuth = false;
    public  Client(Socket client, String username, String password, String processName, int port) {
        this.client = client;
        this.username = username;
        this.pasword = password;
        this.processName = processName;
        this.port = port;
        Runnable target;
        Thread readData = new Thread(new ReadData(this));
        readData.start();

        auth();
    }
    public String getMotd(){
        return motd;
    }
    public String getProcessName() {
        return processName;
    }

    public void auth(){
        sendData(Crypter.encode("#auth"+username+";"+pasword));
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void readData(String data, String name){

        if (data.equalsIgnoreCase("ALREADY!")) {
            System.out.println("Console connecté ");
            auth();
        }
        if(data.equalsIgnoreCase("FAIL!")){
            System.out.println("Console fail connection");
            try {
                this.client.close();
                String pathName;
                if(isProxy()){
                    pathName = "proxy";
                }else {
                    pathName = "server";
                }
                ServerInstance.stopServer(processName,pathName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(data.equalsIgnoreCase("PROXY")){
            System.out.println("PROXY !");
            proxy = true;
        }
        if(data.equalsIgnoreCase("OK!")){
            System.out.println("Vous avez bien été connecté aux serveurs");

            setAuth(true);

            sendData("NAME;"+processName);
            if(proxy){
                Main.getInstance().setProxy(this);
            }else {
                System.out.println("GET! ici");
                sendData("GET");
            }
            String sendServer = "ADDSERVERLIST;"+  getProcessName();
            sendData(sendServer);

            for(Client s : Main.getInstance().getClients()){

                String sendLists = "ADDSERVERLIST;"+  s.getProcessName();
                sendData(sendLists);

                s.sendData("ADDSERVERLIST;"+  getProcessName());

            }

            Main.getInstance().addClient(this);


        }
        if(data.startsWith("START;")){
            String[] args =data.replaceAll(" ","").split(";");
            if(args.length > 2){

            }
        String serverName = args[1];
        String type = args[2];
        String Xms = args[3];
        String Xmx = args[4];
        String pathName;
        if(proxy){
            pathName = "proxy";
        }else {
            pathName = "server";
        }
        ServerInstance.startServer(serverName,pathName, Type.valueOf(type),Xms,Xmx,0);
        }
        if(data.startsWith("STOP;")){
            String serverName = data.split(";")[1];
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            ServerInstance.stopServer(serverName,pathName);
        }
        if(data.startsWith("RESTART;")){
            String[] args =data.replaceAll(" ","").split(";");
            String serverName = args[1];
            String type = args[2];
            String Xms = args[3];
            String Xmx = args[4];
            String port = args[5].replaceAll(" ","");
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            ServerInstance.stopServer(serverName,pathName);
            ServerInstance.startServer(serverName,pathName, Type.valueOf(type),Xms,Xmx, Integer.parseInt(port));
            //ServerInstance.startServer(serverName,pathName,Type.STATIC);
        }
        if(data.startsWith("SENDDATA;")){
            System.out.println("SENDDATA ! => "+ data);
            String serverName = data.split(";")[1];
            String strings = data.replace("SENDDATA;"+serverName+";","");

            Client client = Main.getInstance().getClient(serverName);
            System.out.println(getProcessName()+":"+strings);
            client.sendData(getProcessName()+":"+strings);
        }

        if(data.startsWith("SENDCMD;")){
            String serverName = data.split(";")[1];
            String[] strings = data.replace("SENDCMD;","").split(";");
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            StringBuffer sb = new StringBuffer();

            for (int i = 1; i < strings.length; i++) {
                sb.append(strings[i]+" ");
            }
            Client client = Main.getInstance().getClient(serverName);
            client.sendData("CMD;"+sb.toString());
        }
        if(data.startsWith("RELOAD")){
            this.isReload = true;
            try {
                getClient().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(data.startsWith("GETPROXY;")){
            String subdata = data.replaceAll("GETPROXY;","");

            if(subdata.startsWith("SLOT;")){
                Main.getInstance().getProxy().sendData("SLOT;"+ data.replaceAll("GETPROXY;SLOT;",""));
            }
            if(subdata.startsWith("MAINTENANCE;")){
                Main.getInstance().getProxy().sendData(data.replace("GETPROXY;",""));
                Boolean.parseBoolean(subdata.replace("MAINTENANCE;",""));
            }
            if(subdata.startsWith("ADDMAINTENANCE")){
                Main.getInstance().getProxy().sendData(data.replace("GETPROXY;",""));
            }
            if(subdata.startsWith("REMMAINTENANCE")){
                Main.getInstance().getProxy().sendData(data.replace("GETPROXY;",""));
            }
        }
        System.out.println("["+getProcessName()+"] "+data);
        if(data.startsWith("MOTD")){
            motd = data.split(";")[1];
            System.out.println("SEND !");
            Main.getInstance().getProxy().sendData("START;"+processName+";"+client.getInetAddress().getHostAddress()+";"+(client.getPort()-1)+";"+ motd);
        }

    }
    public void sendData(String data){
            data = Crypter.encode(data);
        if(!client.isClosed()){

            try{
                OutputStream out = client.getOutputStream();

                PrintWriter writer = new PrintWriter(out);
                writer.write(data + "\n");
                writer.flush();
            }catch (Exception e){
                System.out.println("FAIL #7");
            }

        }else {
            System.out.println("Client Connection - Closed");
        }
    }
    public Socket getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public String getPasword() {
        return pasword;
    }

    public boolean isProxy() {
        return proxy;
    }
    //ReadData

    //Auth

}
