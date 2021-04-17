package be.alexandre01.dreamzon.network.license.connection;

import be.alexandre01.dreamzon.network.Start;
import be.alexandre01.dreamzon.network.TemplateLoading;
import be.alexandre01.dreamzon.network.license.connection.socket.LicenseSocket;
import be.alexandre01.dreamzon.network.utils.console.colors.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;

import java.net.Socket;
import java.util.logging.Level;

public class LicenseThreadConnection {
    boolean isFound;
    String adresse;
    int port;
    String username;
    String password;
    String processName;

    int i = 1;
    public LicenseThreadConnection(String adresse, int port, String username, String password, String processName){
        this.adresse = adresse;
        this.port = port;
        this.username = username;
        this.password = password;
        this.processName = processName;
    }
    public void timer(){



                try {

                    Socket socket = new Socket(adresse,port);
                    LicenseSocket client = new LicenseSocket(socket,username,password,processName,port-1);
                    //Utils.setClient(client);
                    //System.out.println("Connécté aux serveur !" + processName);
                    //Console.print("RETURN",Level.INFO);
                    return;
                }catch (Exception e){

                    if(i == 2){
                       Console.print(Colors.ANSI_RED()+"Il semblerait que votre machine n'arrive pas à se connecter à l'architecture.", Level.SEVERE);
                        Console.print(Colors.ANSI_RED()+"Veuillez contacter l'assistance technique sur https://support.dreamnetwork.cloud/", Level.SEVERE);
                        new TemplateLoading();
                        Start.isStarted = true;
                    }else if (i <= 1){
                        System.out.println("Connexion au serveur d'authentification... [TRY#"+i+"]");


                    }
                    i++;
                   //Console.print("OK", Level.INFO);
                    timer();

                }

        }
}

