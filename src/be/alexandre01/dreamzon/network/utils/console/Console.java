package be.alexandre01.dreamzon.network.utils.console;

import be.alexandre01.dreamzon.network.Main;

import java.util.logging.Level;

public class Console {
    public static void print(String s, Level level){
        Main.getLogger().info(s + Colors.ANSI_RESET());
    }
    public static void print(String s){
       System.out.println(s + Colors.ANSI_RESET());
    }
}
