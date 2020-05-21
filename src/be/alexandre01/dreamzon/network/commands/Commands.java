package be.alexandre01.dreamzon.network.commands;


import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;

import java.util.ArrayList;
import java.util.Vector;

public class Commands {

    public ArrayList<CommandsExecutor> executorList;
   public Commands(){
       this.executorList = new ArrayList<>();

   }

   public void addCommands(CommandsExecutor executor){
    this.executorList.add(executor);
   }

   public void check(String[] args){
        boolean hasFound = false;
       for(CommandsExecutor executors : executorList){
               if(executors.onCommand(args)){
                   hasFound = true;
               }


       }
       if(!hasFound){
           Console.print(Colors.ANSI_RED+"La commande n'a pas été trouvé");
       }

   }
}
