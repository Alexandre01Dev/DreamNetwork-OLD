package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Config;
import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.enums.Type;
import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;
import be.alexandre01.dreamzon.network.utils.ServerInstance;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Add implements CommandsExecutor{
    @Override
    public boolean onCommand(String[] args) {

        BufferedWriter processInput = null;
        if(args[0].equalsIgnoreCase("add")){
            clearConsole();
            if(args.length == 3){
                if(args[1].equalsIgnoreCase("server")||args[1].equalsIgnoreCase("proxy")){
                    if(Config.contains("template/"+args[1]+"/"+args[2])){
                        ServerInstance.startServer(args[2],args[1]);
                    }

                }
            }
            if(args.length >= 5){
                 if(args[1].equalsIgnoreCase("server")||args[1].equalsIgnoreCase("proxy")){
                     if(Config.contains("template/"+args[1]+"/"+args[2])){

                         if(args.length == 7){
                             try {
                                 if(args[3].equalsIgnoreCase("STATIC")){
                                     ServerInstance.startServer(args[2],args[1], Type.STATIC,args[4],args[5], Integer.parseInt(args[6]));
                                 }else {
                                     if(args[3].equalsIgnoreCase("DYNAMIC")){
                                         ServerInstance.startServer(args[2],args[1], Type.DYNAMIC,args[4],args[5],Integer.parseInt(args[6]));
                                     }
                                 }
                             }catch (Exception e){
                                 System.out.println("Veuillez marquer le port en chiffre");
                             }
                         }else {
                             if(args[3].equalsIgnoreCase("STATIC")){
                                 ServerInstance.startServer(args[2],args[1], Type.STATIC,args[4],args[5],0);
                             }else {
                                 if(args[3].equalsIgnoreCase("DYNAMIC")){
                                     ServerInstance.startServer(args[2],args[1], Type.DYNAMIC,args[4],args[5],0);
                                 }
                             }
                         }

                     }else {
                         Config.createDir("template/"+args[1]+"/"+args[2]);
                     }


                             /*BufferedReader stdInput = new BufferedReader(new
                                     InputStreamReader(proc.getInputStream()));

                             BufferedReader stdError = new BufferedReader(new
                                     InputStreamReader(proc.getErrorStream()));

                             boolean isLaunch = true;
                             System.out.println("Here is the standard output of the command:\n");
                             String s = null;

                             while ((s = stdInput.readLine()) != null) {
                                 System.out.println("INPUT >> " +s);


                             }
                             stdInput = null;

                             while (isLaunch = true) {


                                 String sa = in.nextLine();
                                 System.out.println("You entered string "+sa);



                             }

                             System.out.println("Here is the standard error of the command (if any):\n");
                             while ((s = stdError.readLine()) != null) {

                                 System.out.println("OUTPUT >>"+s);

                             }
*/


                       //  Console.print(Colors.ANSI_RED+"[!] Ce serveur existe déja");


                 }else {
                     Console.print(Colors.ANSI_RED+"[!] add server [name] [DYNAMIC/STATIC] [XMX] [XMS] (PORT) => add a server ");
                     Console.print(Colors.ANSI_RED+"[!] add proxy [name] [DYNAMIC/STATIC] [XMX] [XMS] (PORT) => add a server ");
                 }
            }else {
                Console.print(Colors.ANSI_RED+"[!] add server [name] [DYNAMIC/STATIC] [XMX] [XMS] (PORT) => add a server ");
            }
            return true;
        }else {
            if(args[0].equalsIgnoreCase("op")){

                try {
                    Main.getInstance().processInput.write("op Alexandre01");
                    Main.getInstance().processInput.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
    public String getStringArgs(String[] args){
        String stringArray[] = args;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < stringArray.length; i++) {
            sb.append(stringArray[i]);
        }
        return Arrays.toString(stringArray);

    }


    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

}