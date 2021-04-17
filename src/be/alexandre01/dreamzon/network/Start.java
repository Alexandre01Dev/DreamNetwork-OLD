package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.utils.console.Console;

import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Start {


    public static boolean isStarted = false;
    public static void main(String[] args){
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("DreamNetwork process shutdown, please wait...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //new License();
        System.out.println("Checking License...");
        //new License();

        try {
            Console.setDefaultConsole("m:default");
            Console.load("m:default");
            Console.setActualConsole("m:default");

            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            String keyS = "BonjouràToi";
            SecureRandom secRandom = new SecureRandom(keyS.getBytes());
            keyGen.init(secRandom);

            //Creating/Generating a key
            Key key = keyGen.generateKey();
          //  new Crypter(key);
            System.out.println();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

                    new TemplateLoading();

       /* new TemplateLoading();
        Console.print(Colors.ANSI_GREEN+" ██████  ██████  ███████  █████  ███    ███ ███    ██ ███████ ████████ ██     ██  ██████  ██████  ██   ██ \n" +
                " ██   ██ ██   ██ ██      ██   ██ ████  ████ ████   ██ ██         ██    ██     ██ ██    ██ ██   ██ ██  ██  \n" +
                " ██   ██ ██████  █████   ███████ ██ ████ ██ ██ ██  ██ █████      ██    ██  █  ██ ██    ██ ██████  █████   \n" +
                " ██   ██ ██   ██ ██      ██   ██ ██  ██  ██ ██  ██ ██ ██         ██    ██ ███ ██ ██    ██ ██   ██ ██  ██  \n" +
                " ██████  ██   ██ ███████ ██   ██ ██      ██ ██   ████ ███████    ██     ███ ███   ██████  ██   ██ ██   ██ \n" +
                "                                                                                                         \n" +Colors.ANSI_CYAN+
                " _   _  __    __   ______      _        \n" +
                "| | | |/  |  /  |  | ___ \\    | |       \n" +
                "| | | |`| |  `| |  | |_/ / ___| |_ __ _ \n" +
                "| | | | | |   | |  | ___ \\/ _ \\ __/ _` |\n" +
                "\\ \\_/ /_| |___| |_ | |_/ /  __/ || (_| |\n" +
                " \\___(_)___(_)___/ \\____/ \\___|\\__\\__,_|\n" +
                "                                                                              "+Colors.ANSI_RED+"                          \uD835\uDCD1\uD835\uDD02 \uD835\uDCD0\uD835\uDCF5\uD835\uDCEE\uD835\uDD01\uD835\uDCEA\uD835\uDCF7\uD835\uDCED\uD835\uDCFB\uD835\uDCEE01                                     \n" +Colors.ANSI_CYAN+
                "                                                                          ");*/

     //   new Main().init();


    }
}
