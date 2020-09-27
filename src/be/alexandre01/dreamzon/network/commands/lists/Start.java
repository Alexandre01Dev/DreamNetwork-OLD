package be.alexandre01.dreamzon.network.commands.lists;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.commands.CommandsExecutor;
import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.ServerInstance;

import java.util.logging.Level;

public class Start implements CommandsExecutor {

    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("start")){
            if(args.length == 3){
                if(args[1].equalsIgnoreCase("server")||args[1].equalsIgnoreCase("proxy")){
                    if(Config.contains("template/"+args[1]+"/"+args[2])){
                        Server process = new Server(args[2],args[1]);
                        process.startServer();
                      //ServerInstance.startServer(args[2],args[1]);
                    }else {
                        Console.print(Colors.ANSI_RED()+"Veuillez d'abord configurer votre serveur avant de faire cela", Level.WARNING);
                    }
                }else {
                    Console.print(Colors.ANSI_RED()+"start [SERVER OR PROXY] ServerName",Level.WARNING);
                }
            }
            return true;
        }
        return false;
    }
}
