package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;
import be.alexandre01.dreamzon.network.utils.ServerInstance;

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
            if(args.length == 3){
                if(args[1].equalsIgnoreCase("server")||args[1].equalsIgnoreCase("proxy")){
                    if(Config.contains("template/"+args[1]+"/"+args[2])){
                        ServerInstance.startServer(args[2],args[1]);
                    }else {
                        Console.print(Colors.ANSI_RED+"Veuillez d'abord configurer votre serveur avant de faire cela");
                    }
                }else {
                    Console.print(Colors.ANSI_RED+"start [SERVER OR PROXY] ServerName");
                }
            }
            return true;
        }
        return false;
    }
}
