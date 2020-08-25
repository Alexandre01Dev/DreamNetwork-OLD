package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.connection.client.Client;
import be.alexandre01.dreamzon.network.utils.console.Colors;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.message.Message;

import java.util.logging.Level;

public class CMD implements CommandsExecutor{

    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("cmd")){
            if(args.length >= 2){
            if(Main.getInstance().getClient(args[1]) != null){
                Client client = Main.getInstance().getClient(args[1]);


             StringBuffer sb = new StringBuffer();

             for (int i = 2; i < args.length; i++) {
                 sb.append(args[i]+" ");
             }



             client.sendData(new Message().set("CMD",sb.toString()));
            System.out.println(sb.toString());
            }else {
                Console.print(Colors.ANSI_RED()+"Veuillez préciser le serveur", Level.WARNING);
            }
         }else {
             System.out.println("Vous n'avez pas de serveur connecté");
         }
         return true;
        }
        return false;
    }
}
