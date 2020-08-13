package be.alexandre01.dreamzon.network;

import be.alexandre01.dreamzon.network.utils.Colors;
import be.alexandre01.dreamzon.network.utils.Console;
import be.alexandre01.dreamzon.network.utils.Utils;

import java.io.File;
import java.util.logging.Level;

public class TemplateLoading {
    File[] directories = new File(Config.getPath("template/server/")).listFiles(File::isDirectory);
    public TemplateLoading(){
        System.out.println("Loading templates...");
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Config.createDir("template");
        if(directories != null){
            for(File dir : directories){
                if(Config.contains(dir.getAbsolutePath()+"/spigot.jar") && Config.contains(dir.getAbsolutePath()+"/network.yml") ){
                    Console.print(Colors.ANSI_GREEN()+"[✓] Template "+ dir.getName()+" loaded !", Level.ALL);
                    Utils.templates.add(dir.getName());
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    Console.print(Colors.ANSI_RED()+"[!] Template "+ dir.getName()+" is not configured !",Level.ALL);
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


            Console.print(Colors.ANSI_GREEN()+" ██████  ██████  ███████  █████  ███    ███ ███    ██ ███████ ████████ ██     ██  ██████  ██████  ██   ██ \n" +
                    " ██   ██ ██   ██ ██      ██   ██ ████  ████ ████   ██ ██         ██    ██     ██ ██    ██ ██   ██ ██  ██  \n" +
                    " ██   ██ ██████  █████   ███████ ██ ████ ██ ██ ██  ██ █████      ██    ██  █  ██ ██    ██ ██████  █████   \n" +
                    " ██   ██ ██   ██ ██      ██   ██ ██  ██  ██ ██  ██ ██ ██         ██    ██ ███ ██ ██    ██ ██   ██ ██  ██  \n" +
                    " ██████  ██   ██ ███████ ██   ██ ██      ██ ██   ████ ███████    ██     ███ ███   ██████  ██   ██ ██   ██ \n" +
                    "                                                                                                         \n" +Colors.ANSI_CYAN+
                    " _   _  __    __   ______      _        \n" +
                    "| | | |/  |  /  |  | ___ \\    | |       \n" +
                    "| | | |`| |  `| |  | |_/ / ___| |_ __ _ \n" +
                    "| | | | | |   | |  | ___ \\/ _ \\ __/ _` |\n" +
                    "\\ \\_/ /_| |___| |_ | |_/ /  __/ || (_| |\n" +
                    " \\___(_)___(_)___/ \\____/ \\___|\\__\\__,_|\n" +
                    "                                                                              "+Colors.ANSI_RED()+"                          \uD835\uDCD1\uD835\uDD02 \uD835\uDCD0\uD835\uDCF5\uD835\uDCEE\uD835\uDD01\uD835\uDCEA\uD835\uDCF7\uD835\uDCED\uD835\uDCFB\uD835\uDCEE01                                     \n" +Colors.ANSI_CYAN+
                    "                                                                          ");

              new Main().init();
        }else {
            return;
        }

    }
}
