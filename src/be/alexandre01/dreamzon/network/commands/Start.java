package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Start  implements CommandsExecutor{

    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("start")){
            if(args.length >= 2){
                if(args[1].equalsIgnoreCase("")){

                }
            }
            return true;
        }
        return false;
    }
}
