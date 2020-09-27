package be.alexandre01.dreamzon.network.commands;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.commands.lists.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandReader extends Thread {
    Commands commands;
    BufferedReader reader;
    private String[] args;
    private boolean stop = false;

    public CommandReader(InputStream in){
        commands = new Commands();
        reader = new BufferedReader(new InputStreamReader(in));
        write("> ");

        try {
            args = reader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        commands.addCommands(new Help());
        commands.addCommands(new Add());
        commands.addCommands(new Remove());
        commands.addCommands(new Connection());
        commands.addCommands(new CMD());
        commands.addCommands(new Start());
        commands.addCommands(new Remote());
        commands.addCommands(new Screen());
    }

    @Override
    public void run(){
        while (!stop){
            if(args.length != 0){
                if(!args[0].equalsIgnoreCase(" ")){
                    commands.check(args);
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
