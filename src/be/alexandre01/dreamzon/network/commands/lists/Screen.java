package be.alexandre01.dreamzon.network.commands.lists;

import be.alexandre01.dreamzon.network.commands.CommandsExecutor;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.screen.ScreenManager;

import java.util.logging.Level;

public class Screen implements CommandsExecutor {
    @Override
    public boolean onCommand(String[] args) {
        if(!args[0].equalsIgnoreCase("screen")){
            return false;
        }else {
            System.out.println("ok");
        }

        if(args.length == 1){
            notComplete();
            return true;
        }
        System.out.println("C'est ->" +ScreenManager.instance);
        if(ScreenManager.instance.containsScreen(args[1])){
            ScreenManager.instance.watch(args[1]);
            System.out.println("Connected to screen");
        }else {
            Console.print("There is no screen",Level.ALL);
        }
        return true;
    }

    private void notComplete(){
        Console.print(Colors.RED+"screen servername", Level.ALL);
    }
}
