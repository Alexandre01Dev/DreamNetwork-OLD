package be.alexandre01.dreamzon.network.utils.console;

import be.alexandre01.dreamzon.network.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;

public class Console extends Thread {
    private static Console instance;
    private InputStream in;
    private final ArrayList<String> history;
    public static void load(InputStream in){
        if(instance!=null)
            instance = new Console(in);
    }
    public static void print(String s, Level level){
        Main.getLogger().info(s + Colors.ANSI_RESET());
    }
    public static void print(String s){
       System.out.println(s + Colors.ANSI_RESET());
    }
    public static void clearConsole(){
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception ignored)
        {

        }
    }
    public Console(InputStream in){
        this.in = in;
        this.history = new ArrayList<>();
    }
    @Override
    public void run() {
        String data = null;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        try {
            while (true){
                while ((data = bufferedReader.readLine()) != null){
                    if(history.size() > 25){
                        history.remove(0);
                    }
                    history.add(data);
                }
            }
        }catch (Exception e){

        }
    }

    public ArrayList<String> getHistory() {
        return history;
    }
}
