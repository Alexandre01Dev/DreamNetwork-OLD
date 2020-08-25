package be.alexandre01.dreamzon.network.license.connection.socket;

import be.alexandre01.dreamzon.network.Start;
import be.alexandre01.dreamzon.network.TemplateLoading;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

public class LicenseSocket {
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
    public LicenseSocket(Socket client, String username, String password, String processName, int port) {
        this.client = client;
        this.username = username;
        this.pasword = password;
        this.processName = processName;
        this.port = port;
        Runnable target;
        Thread readData = new Thread(new ReadData(this));
        readData.start();

    }
    public String getMotd(){
        return motd;
    }
    public String getProcessName() {
        return processName;
    }

    public void auth(){
        sendData(BasicCrypter.encode("#auth"+username+";"+pasword));
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void readData(String data){
        if(data.contains("ASK")){
            sendData("CHECK;BENJI-LE-BG");
        }
        if(data.contains("LICENSESWASBEENCHECKED")){
            Console.print(Colors.ANSI_GREEN()+"La license à été vérifié avec succès", Level.FINE);
            if(!Start.isStarted){
                Start.isStarted = true;
                new TemplateLoading();
            }

        }
        if(data.contains("LICENSESWASINCORRECT")){
            Console.print(Colors.ANSI_RED()+"La license à été refusé",Level.SEVERE);
            System.exit(0);
        }

    }
    public void sendData(String data){
        data = BasicCrypter.encode(data);
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
}
