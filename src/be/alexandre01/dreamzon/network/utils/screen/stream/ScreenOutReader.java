package be.alexandre01.dreamzon.network.utils.screen.stream;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.commands.Commands;
import be.alexandre01.dreamzon.network.commands.lists.*;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.screen.Screen;
import be.alexandre01.dreamzon.network.utils.screen.commands.ScreenCommands;
import be.alexandre01.dreamzon.network.utils.screen.commands.ScreenExit;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScreenOutReader{
    ScreenCommands commands;
    BufferedReader reader;
    private String[] args;
    private Screen screen;
    private Console console;
    public boolean stop = false;
    public ScreenOutReader(Screen screen, Console console){
        this.console = console;
        this.screen = screen;
        commands = new ScreenCommands(screen);
        /*reader = new BufferedReader(new InputStreamReader(screen.getScreenStream().in));
        write("> ");

        try {
            args = reader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        commands.addCommands(new ScreenExit(screen));

        run();
    }

    public void run(){
        Console.debugPrint("openRUn");
        console.setConsoleAction(new Console.IConsole() {
            @Override
            public void listener(String[] args) {
             //   Console.debugPrint(String.valueOf(args.length));
                if(args.length != 0){
                    //Console.debugPrint("capte");
                    boolean hasFound = false;
                    if(!args[0].equalsIgnoreCase(" ")){
                        //Console.debugPrint(Arrays.toString(args));
                        if(!commands.check(args)){
                            try {
                                //Console.debugPrint("start");
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter( screen.getServer().getProcessus().getOutputStream()));
                                //Console.debugPrint("writer");
                                for (int i = 0; i < args.length-1; i++) {
                                    writer.write(args[i]+" ");
                               }
                                writer.write(args[args.length-1]+"\n");

                               //writer.write(reader.readLine()+"\n");
                              //  Console.debugPrint("write");
                                 writer.flush();
                                // Console.debugPrint("flush");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });


//            write("> ");
         /*   try {
                args = reader.readLine().split(" ");
            }catch (Exception e){

            }*/
        }


    public void write(String str){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Main.getInstance().formatter.getDefaultStream().write(stringToBytesASCII(str));
                    scheduler.shutdown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },50,50, TimeUnit.MILLISECONDS);

    }
    public static byte[] stringToBytesASCII(String str) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) buffer[i];
        }
        return b;
    }

}
