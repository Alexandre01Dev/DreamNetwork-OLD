package be.alexandre01.dreamzon.network.utils.screen.commands;

import be.alexandre01.dreamzon.network.commands.CommandsExecutor;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.screen.Screen;
import be.alexandre01.dreamzon.network.utils.screen.stream.ScreenStream;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class ScreenCommands {
    public ArrayList<CommandsExecutor> executorList;
    private Screen screen;
    public ScreenCommands(Screen screen){
        this.executorList = new ArrayList<>();
        this.screen = screen;
    }

    public void addCommands(CommandsExecutor executor){
        this.executorList.add(executor);
    }

    public boolean check(String[] args){
        boolean hasFound = false;
        for(CommandsExecutor executors : executorList){
            if(executors.onCommand(args)){
                hasFound = true;
            }
        }
        if(!hasFound){
            return false;
          //  screen;
        }
        return true;
    }
}
