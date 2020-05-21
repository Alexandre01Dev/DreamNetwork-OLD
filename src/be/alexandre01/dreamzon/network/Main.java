package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.commands.*;
import be.alexandre01.dreamzon.network.commands.Start;
import be.alexandre01.dreamzon.network.proxy.server.Proxy;
import be.alexandre01.dreamzon.network.remote.client.Client;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {
    private static String[] args;
    private static Main instance;
    private static Config config;
    public boolean isRunning = false;
    public BufferedWriter processInput = null;
    private Commands commands;
    private ArrayList<Client> clients = new ArrayList<>();
    public boolean isRemote = false;
    private Client proxy;

    public void addClient(Client client){
        this.clients.add(client);
    }
    public Client getClient(String name) {
        for (Client clients : clients){
            if(clients.getProcessName().equals(name)){
                return clients;
            }
        }
     return null;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }
    public Client getProxy(){
        return proxy;
    }

    public void setProxy(Client proxy) {
        this.proxy = proxy;
    }

    public void init(){
        commands = new Commands();
        instance = this;

        try{
            System.setProperty("file.encoding","UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);


            instance = this;
            config = new Config();
            if(!System.getProperty("java.specification.version").equals("1.8")){
                System.out.println("!!! La version de votre java n'est pas la version 8 (1.8)");
                System.out.println("Votre version => "+System.getProperty("java.specification.version"));
                Thread.sleep(1000*5);
            }
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Tap [Enter]");
            String question = reader.readLine();
            System.out.println("Le Network a été démarré avec succès / Faites help pour avoir les commandes");
            BufferedReader cReader =
                    new BufferedReader(new InputStreamReader(System.in));

            args = reader.readLine().split(" ");
            commands.addCommands(new Help());
            commands.addCommands(new Add());
            commands.addCommands(new Remove());
            commands.addCommands(new Connection());
            commands.addCommands(new CMD());
            commands.addCommands(new Start());
            commands.addCommands(new Remote());
            isRunning= true;
            while (isRunning){
                commands.check(args);
                args = reader.readLine().split(" ");
            Config.createDir("template");
            }
            //copy source to target using Files Class
            Config.copy(new File(Config.getPath("template")),new File(Config.getPath("template1")));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Main getInstance(){
        return instance;
    }
    public static Config getConfig(){
        return config;
    }
    public String[] getArgs(){ return args;}
}

