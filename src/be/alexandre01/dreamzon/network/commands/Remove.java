package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;

public class Remove implements CommandsExecutor{
    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("remove")){
            if(args.length >= 2){
                if(args[1].equalsIgnoreCase("server")||args[1].equalsIgnoreCase("proxy")){
                    String name = args[2];
                    if(Config.contains("template/"+args[1].toLowerCase()+"/"+name)){
                        Config.removeDir("template/"+args[1].toLowerCase()+"/"+name);
                        Console.print(Colors.ANSI_BLUE+"[V] Ce serveur a été supprimé");
                        Console.print(Colors.ANSI_BLUE+"Le dossier a été supprimé dans le dossier 'template' ");
                    }else {
                        Console.print(Colors.ANSI_RED+"[!] Ce serveur n'existe pas");
                    }


                }else {
                    Console.print(Colors.ANSI_RED+"[!] remove server [name] => remove a server ");
                    Console.print(Colors.ANSI_RED+"[!] remove proxy [name] => remove a server ");
                }
            }else {
                Console.print(Colors.ANSI_RED+"[!] remove server [name] => remove a server ");
            }
            return true;
        }
        return false;
    }
}
