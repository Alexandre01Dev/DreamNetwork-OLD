package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;

public class Help implements CommandsExecutor {

    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("help")){
            Console.print(Colors.ANSI_BLUE+"add server [name] => add a server ");
            Console.print(Colors.ANSI_BLUE+"add proxy [name] => add a server ");
            Console.print("remove server [name] => remove a server ");
            Console.print("remove proxy [name] => remove a server ");
            Console.print("start server [name] => start a server ");
            Console.print("stop server [name] => add a server ");
            Console.print("restart server [name] => add a server ");
            return true;
        }
        return false;

    }
}
