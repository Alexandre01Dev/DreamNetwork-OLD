package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;
import be.alexandre01.dreamzon.network.utils.Utils;

import java.io.File;

public class TemplateLoading {
    File[] directories = new File(Config.getPath("template/server/")).listFiles(File::isDirectory);
    public TemplateLoading(){
        Config.createDir("template");
        if(directories != null){
            for(File dir : directories){
                if(Config.contains(dir.getAbsolutePath()+"/spigot.jar") && Config.contains(dir.getAbsolutePath()+"/network.yml") ){
                    Console.print(Colors.ANSI_GREEN+"[✓] Template "+ dir.getName()+" loaded !");
                    Utils.templates.add(dir.getName());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    Console.print(Colors.ANSI_RED+"[!] Template "+ dir.getName()+" is not configured !");
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Console.print(Colors.ANSI_PURPLE+"------------------------------------");
            Console.print(Colors.ANSI_CYAN+"Starting...");
            Console.print(Colors.ANSI_PURPLE+"------------------------------------");
        }else {
            return;
        }

    }
}
