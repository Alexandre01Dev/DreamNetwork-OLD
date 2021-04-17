package be.alexandre01.dreamzon.network.commands.lists;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.commands.CommandsExecutor;
import be.alexandre01.dreamzon.network.utils.console.colors.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;

import java.util.logging.Level;

public class Remove implements CommandsExecutor {
    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("remove")){
            if(args.length >= 2){
                if(args[1].equalsIgnoreCase("server")||args[1].equalsIgnoreCase("proxy")){
                    String name = args[2];
                    if(Config.contains("template/"+args[1].toLowerCase()+"/"+name)){
                        Config.removeDir("template/"+args[1].toLowerCase()+"/"+name);
                        Console.print(Colors.ANSI_BLUE+"[V] Ce serveur a été supprimé", Level.INFO);
                        Console.print(Colors.ANSI_BLUE+"Le dossier a été supprimé dans le dossier 'template' ", Level.INFO);
                    }else {
                        Console.print(Colors.ANSI_RED()+"[!] Ce serveur n'existe pas", Level.WARNING);
                    }


                }else {
                    Console.print(Colors.ANSI_RED()+"[!] remove server [name] => remove a server ", Level.INFO);
                    Console.print(Colors.ANSI_RED()+"[!] remove proxy [name] => remove a server ", Level.INFO);
                }
            }else {
                Console.print(Colors.ANSI_RED()+"[!] remove server [name] => remove a server", Level.INFO);
            }
            return true;
        }
        return false;
    }
}
