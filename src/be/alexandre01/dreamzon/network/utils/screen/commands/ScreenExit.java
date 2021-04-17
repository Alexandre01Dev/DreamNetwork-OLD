package be.alexandre01.dreamzon.network.utils.screen.commands;

import be.alexandre01.dreamzon.network.commands.CommandsExecutor;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.screen.Screen;

import java.util.Arrays;

public class ScreenExit implements CommandsExecutor {
    Screen screen;
    public ScreenExit(Screen screen){
        this.screen = screen;
    }
    @Override
    public boolean onCommand(String[] args) {
     //   Console.debugPrint("Exit >>"+ Arrays.asList(args));
        if(args[0].equalsIgnoreCase(":exit")){
            Console.debugPrint("exit ! ");
            Console.setActualConsole("m:default");
           screen.getScreenStream().exit();
            return true;
        }
        return false;
    }
}
