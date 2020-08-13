package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.license.License;
import be.alexandre01.dreamzon.network.license.LicenseSocket;
import be.alexandre01.dreamzon.network.utils.*;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

public class Start {
    public static PrintStream defaultStream;

    public static boolean isStarted = false;
    public static void main(String[] args){
        try {
            System.setProperty("file.encoding","UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);
        }catch (Exception e){
            e.printStackTrace();
        }


        AnsiConsole.systemUninstall();
        defaultStream = System.out;
        ByteArrayOutputStream loggerContent = new LoggingOutputStream(Main.getLogger(), Level.ALL);
         PrintStream prStr = new Interceptor(loggerContent);
        StreamHandler streamHandler = new StreamHandler(prStr, new ConciseFormatter(false));

        final PrintStream err = System.err;
        Main.getLogger().setUseParentHandlers(false);
        try {
            ConciseFormatter c = new ConciseFormatter(false);
            ConsoleHandler handler = new ConsoleHandler();
            try {
                handler.setEncoding("UTF-8");
                System.out.println("Oh fuck");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            handler.setFormatter(c);
            handler.flush();
            Main.getLogger().addHandler( handler);
            System.setOut(prStr);
            System.setErr(System.out);


        } finally {
            System.setErr(err);
        }

        //new License();
        System.out.println("Checking License...");
        new License();
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom secRandom = new SecureRandom();
            keyGen.init(secRandom);

            //Creating/Generating a key
            Key key = keyGen.generateKey();
            System.out.println(key.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }



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
