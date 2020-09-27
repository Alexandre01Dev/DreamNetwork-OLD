package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.commands.*;
import be.alexandre01.dreamzon.network.commands.lists.*;
import be.alexandre01.dreamzon.network.client.Client;
import be.alexandre01.dreamzon.network.commands.lists.Start;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.console.Formatter;
import be.alexandre01.dreamzon.network.utils.screen.Screen;
import be.alexandre01.dreamzon.network.utils.screen.ScreenManager;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


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
    private InputStream in;
    public Formatter formatter;
    public static Logger logger = Logger.getLogger(Main.class.getName());
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

    //INITIALISATION DU DREAMNETWORK
    public void init(){
        in = System.in;
        commands = new Commands();
        instance = this;
        Config.createDir("template");
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
                    new BufferedReader(new InputStreamReader(in));
           // System.out.println("Tap [Enter]");
            System.out.println("DreamNetwork can be run in different mode:\n");
            Console.print("Please type the number of your choice.\n");
            Console.print("(1): Normal mode "+Colors.ANSI_GREEN()+"[Operational]");
            Console.print("(2) Multiserver mode "+Colors.ANSI_RED()+"[Not operational]");
            Console.print("(3) Normal + multiserver mode "+ Colors.ANSI_RED()+"[Not operational]");
            Console.print("!(4) Normal mode Debug");

            //NUMBER QUESTONS
            String question = reader.readLine();


            formatter = new Formatter();
            formatter.format();

            //Screen Manager
            ScreenManager.load();

            System.out.println("Le Network a été démarré avec succès / Faites help pour avoir les commandes");
            Thread commandReader = new Thread( new CommandReader(in));
            commandReader.start();
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

    public static Logger getLogger() {
        return logger;
    }

    public static byte[] stringToBytesASCII(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) buffer[i];
        }
        return b;
    }

    public void write(String str){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    formatter.getDefaultStream().write(stringToBytesASCII(str));
                    scheduler.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },50,50, TimeUnit.MILLISECONDS);

    }
}

