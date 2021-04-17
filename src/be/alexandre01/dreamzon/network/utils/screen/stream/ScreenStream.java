package be.alexandre01.dreamzon.network.utils.screen.stream;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.screen.Screen;

import java.io.*;
import java.util.function.Consumer;

public class ScreenStream {
    public PrintStream oldOut = System.out;
    public InputStream oldIn = System.in;
    public ScreenInput in;
    public PrintStream out;
    public boolean isInit;
    Console console;
    ScreenInReader screenInReader;
    ScreenOutReader screenOutReader;
    public void init(String name, Screen screen){
        Console console = Console.load("s:"+name);
        Console.setActualConsole("s:"+name);
        this.console = console;
        Thread screenInReader = new Thread(new ScreenInReader(console,screen.getServer()));
        screenInReader.start();
        this.screenOutReader = new ScreenOutReader(screen,console);

    /*    ByteArrayOutputStream screenOutput = new ByteArrayOutputStream( );
        byte[] bytes = screenOutput.toByteArray();
        in = new ScreenInput(bytes);
        out = new PrintStream(screenOutput);
        System.setIn(in.inputStream);
        System.out.println("in sysout");
        Console.print("in console print");
        try {
            screenOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("out open test");
       Main.getInstance().formatter.getDefaultStream().println("lol");
        System.out.println("out sysout");
        Console.print("out console print");
        isInit = true;*/
    }

    public void exit(){
        console.destroy();
        screenOutReader.stop = true;
        screenInReader.destroy();
  /*      isInit = false;
        Console.clearConsole();
        System.setIn(oldIn);
        System.setOut(oldOut);*/
    }
}
