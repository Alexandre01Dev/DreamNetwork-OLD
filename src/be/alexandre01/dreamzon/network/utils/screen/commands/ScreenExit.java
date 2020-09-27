package be.alexandre01.dreamzon.network.utils.screen.commands;

import be.alexandre01.dreamzon.network.commands.CommandsExecutor;
import be.alexandre01.dreamzon.network.utils.screen.Screen;

public class ScreenExit implements CommandsExecutor {
    Screen screen;
    public ScreenExit(Screen screen){
        this.screen = screen;
    }
    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase(":exit")){
            screen.destroy();
            return true;
        }
        return false;
    }
}
