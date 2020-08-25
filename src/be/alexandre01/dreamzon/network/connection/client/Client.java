package be.alexandre01.dreamzon.network.connection.client;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.enums.Mods;
import be.alexandre01.dreamzon.network.utils.*;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

public class Client extends Remote {
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
        super(Type.Client,client);
        this.client = client;
        this.username = username;
        this.pasword = password;
        this.processName = processName;
        this.port = port;
        Runnable target;
        setupChannels();
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

    public void setupChannels(){
        MessageChannel messageChannel = new MessageChannel("DNServerConfigurator",client);
        messageChannel.setupActions(new MessageChannel.ReadChannel() {
            @Override
            public void read(Message message, MessageChannel channel) {
                System.out.println("Channel"+message);
                if(message.contains("STEP-1")){
                    message.getBoolean("OK");
                }
            }
        });

        MessageChannel genie = new MessageChannel("JadoreGenie",client);
        genie.setupActions(new MessageChannel.ReadChannel() {
            @Override
            public void read(Message message, MessageChannel channel) {
                System.out.println(message.getBoolean("GenieEstBeau"));
            }
        });


    }
    public void auth(){
        Message message = new Message();
        message.set("AUTH",true);
        message.set("USERNAME",username);
        message.set("PASSWORD",pasword);
        sendData(message);
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void readData(Message data,String server){
        System.out.println("YES");
        System.out.println(data);
        if (data.contains("ALREADY")) {
            Console.print("Console connecté ", Level.INFO);
            auth();
        }
        if(data.contains("FAIL")){
            Console.print("Console fail connection",Level.WARNING);
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
        if(data.contains("PROXY")){
            proxy = true;
        }
        if(data.contains("OK!")){
            Console.print(Colors.GREEN+"Vous avez bien été connecté aux serveurs",Level.INFO);
            setAuth(true);

            sendData(new Message().set("NAME",processName));
            if(proxy){
                Main.getInstance().setProxy(this);
            }else {
               // System.out.println("GET! ici");
                sendData(new Message().set("GET",true));
            }
            Message sendServer = new Message().set("ADDSERVERLIST",getProcessName());
            sendData(sendServer);
            ArrayList<String> template = new ArrayList<>();
            for(Client s : Main.getInstance().getClients()){

                String sendLists = "ADDSERVERLIST;"+  s.getProcessName();
                template.add(s.getProcessName().split("-")[0]);

                s.sendData(new Message().set("ADDSERVERLIST", s.getProcessName()));
                if(!proxy){
                    Message message = new Message();
                    message.set("START",true);
                    message.set("ServerName",processName);
                    message.set("IP",client.getInetAddress().getHostAddress());
                    message.set("Port",client.getPort());
                    message.set("MOTD",client.getPort());
                    Main.getInstance().getProxy().sendData(message);
                }
            }

            sendData(new Message().set("ADDTEMPLATELIST",template));

            Main.getInstance().addClient(this);
            if(!proxy){
                Message message = new Message();
                message.set("START",true);
                message.set("ServerName",processName);
                message.set("IP",client.getInetAddress().getHostAddress());
                message.set("PORT",client.getPort());
                message.set("MOTD",motd);
                Main.getInstance().getProxy().sendData(message);
            }


        }

        if(data.contains("START")){
        String serverName = data.getString("SERVERNAME");
        String type = data.getString("TYPE");
        String Xms = data.getString("XMS");
        String Xmx = data.getString("XMX");
        String pathName;
        if(proxy){
            pathName = "proxy";
        }else {
            pathName = "server";
        }
        ServerInstance.startServer(serverName,pathName, Mods.valueOf(type),Xms,Xmx,0);
        }
        if(data.contains("STOP")){
            String serverName = data.getString("STOP").split(";")[0];
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            ServerInstance.stopServer(serverName,pathName);
        }
        if(data.contains("RESTART")){
            String[] args =data.getString("RESTART").replaceAll(" ","").split(";");
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
            ServerInstance.startServer(serverName,pathName, Mods.valueOf(type),Xms,Xmx, Integer.parseInt(port));
            //ServerInstance.startServer(serverName,pathName,Type.STATIC);
        }
        if(data.contains("SENDDATA")){
          //  System.out.println("SENDDATA ! => "+ data);
            Message message = new Message();
            message.set("PROVIDER",getProcessName());
            message.set("DATA",data.get("DATA"));
            String serverName = data.getString("SENDDATA").split(";")[0];
            Client client = Main.getInstance().getClient(data.getString("SERVER"));
           // System.out.println(getProcessName()+":"+strings);
            client.sendData(message);
        }

        if(data.contains("SENDCMD")){

            String serverName =  data.getString("SERVERNAME");
            String cmd =   data.getString("COMMAND");
            String pathName;
            if(proxy){
                pathName = "proxy";
            }else {
                pathName = "server";
            }
            Client client = Main.getInstance().getClient(serverName);
            client.sendData(data);
        }
        if(data.contains("RELOAD")){
            this.isReload = true;
            try {
                getClient().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(data.contains("TEST")){
        }
        if(data.contains("GETPROXY")){
            if(data.contains("SLOT")){
                Message message = new Message();
                message.set("SLOT",data.getInt("SLOT"));
                Main.getInstance().getProxy().sendData(message);
            }
            if(data.contains("MAINTENANCE")){
                Message message = new Message();
                message.set("MAINTENANCE",data.get("MAINTENANCE"));
                Main.getInstance().getProxy().sendData(message);
            }
            if(data.contains("ADDMAINTENANCE")){
                Main.getInstance().getProxy().sendData(new Message().set("ADDMAINTENANCE",data.get("ADDMAINTENANCE")));
            }
            if(data.contains("REMMAINTENANCE")){
                Main.getInstance().getProxy().sendData(new Message().set("REMMAINTENANCE",data.get("REMMAINTENANCE")));
            }
        }
        //Console.print("["+getProcessName()+"] "+data,Level.INFO);
        if(data.contains("MOTD")){
            motd = data.getString("MOTD").split(";")[0];
           //System.out.println("SEND !");
            Message message = new Message();
            message.set("SERVERNAME",processName);
            message.set("IP",client.getInetAddress().getHostAddress());
            message.set("PORT",client.getPort());
            message.set("MOTD",motd);
            Main.getInstance().getProxy().sendData(message);
        }

    }
    public void sendData(Message data){
            String encode = BasicCrypter.encode(data.toString());
        if(!client.isClosed()){

            try{
                OutputStream out = client.getOutputStream();

                PrintWriter writer = new PrintWriter(out);
                assert encode != null;
                writer.write(encode+"\n");
                writer.flush();
            }catch (Exception e){
                System.out.println("FAIL #7");
            }

        }else {
            Console.print("Client Connection - Closed",Level.WARNING);
        }
    }
    public Socket getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return pasword;
    }

    public boolean isProxy() {
        return proxy;
    }

    public void sendInfo(){
        Message message = new Message();
        MessageChannel messageChannel = new MessageChannel("DNServerConfigurator");
        message.set("NAME",processName);
        message.set("","");
    }
    //ReadData

    //Auth

}
