package be.alexandre01.dreamzon.network.utils.screen;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.utils.console.Console;

import java.util.HashMap;

public class ScreenManager {
    private HashMap<String,Screen> screens;
    public static ScreenManager instance;

    public static void load(){
        if(instance==null)
            instance = new ScreenManager();

    }
    public ScreenManager(){
        screens = new HashMap<>();
    }
    public void addScreen(Screen screen){
        System.out.println("Screen name -> "+ screen.server.getName());
        screens.put(screen.server.getName(),screen);
    }
    public void remScreen(Screen screen){
        screens.remove(screen.server.getName());
    }
    public boolean containsScreen(String s){
        return screens.containsKey(s);
    }
    public void watch(String server){
        Console.clearConsole();
        screens.get(server).screenStream.init();
    }
}
