package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.remote.client.Client;
import be.alexandre01.dreamzon.network.utils.ServerInstance;
import be.alexandre01.dreamzon.network.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Remote implements CommandsExecutor {
    @Override
    public boolean onCommand(String[] args) {
        if(args[0].equalsIgnoreCase("remote")){
            if(!Main.getInstance().isRemote){
                Main.getInstance().isRemote = true;
            }else {
                Main.getInstance().isRemote = false;
            }

            while (Main.getInstance().isRemote){


            //Process proc = ServerInstance.getProcess("Spigot-0");

            BufferedReader bufferedReader = ServerInstance.getProcessInput("Spigot-0");
                try {
                            String s =null;
                            while ((s = bufferedReader.readLine()) != null) {
                                System.out.println(s);
                            }
                        }catch (Exception e){

                        }


            }
            return true;
        }



        return false;
    }
    public void invoke(String line){
        System.out.println(line);
    }
}
