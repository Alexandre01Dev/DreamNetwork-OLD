package be.alexandre01.dreamzon.network.utils.screen.stream;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.commands.Commands;
import be.alexandre01.dreamzon.network.commands.lists.*;
import be.alexandre01.dreamzon.network.utils.screen.Screen;
import be.alexandre01.dreamzon.network.utils.screen.commands.ScreenCommands;
import be.alexandre01.dreamzon.network.utils.screen.commands.ScreenExit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScreenOutReader extends Thread{
    ScreenCommands commands;
    BufferedReader reader;
    private String[] args;
    private Screen screen;
    public ScreenOutReader(Screen screen){
        this.screen = screen;
        commands = new ScreenCommands(screen);
        reader = new BufferedReader(new InputStreamReader(screen.getScreenStream().in));
        write("> ");

        try {
            args = reader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        commands.addCommands(new ScreenExit(screen));
    }

    @Override
    public void run(){
        while (super.isAlive()){
            if(args.length != 0){
                boolean hasFound = false;
                if(!args[0].equalsIgnoreCase(" ")){
                    if(!commands.check(args)){
                        try {
                            screen.getServer().getProcessus().getOutputStream().write(stringToBytesASCII(reader.readLine()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            write("> ");

            try {
                args = reader.readLine().split(" ");
            }catch (Exception e){

            }
        }
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
