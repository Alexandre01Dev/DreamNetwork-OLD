package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.utils.console.colors.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;

import java.util.logging.Level;

public class Help implements CommandsExecutor {

    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("help")){
            Console.print(Colors.ANSI_BLUE+"add server [name] => add a server ", Level.INFO);
            Console.print(Colors.ANSI_BLUE+"add proxy [name] => add a server ", Level.INFO);
            Console.print("remove server [name] => remove a server ", Level.INFO);
            Console.print("remove proxy [name] => remove a server ", Level.INFO);
            Console.print("start server [name] => start a server ", Level.INFO);
            Console.print("stop server [name] => add a server ", Level.INFO);
            Console.print("restart server [name] => add a server ", Level.INFO);
            return true;
        }
        return false;

    }
}
