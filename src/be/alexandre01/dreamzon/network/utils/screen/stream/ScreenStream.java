package be.alexandre01.dreamzon.network.utils.screen.stream;

import be.alexandre01.dreamzon.network.utils.console.Console;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class ScreenStream {
    public PrintStream oldOut = System.out;
    public InputStream oldIn = System.in;
    public ScreenInput in;
    public PrintStream out;
    public boolean isInit;
    public void init(){
        ScreenOutput screenOutput = new ScreenOutput();
        byte[] bytes = screenOutput.toByteArray();
        in = new ScreenInput(bytes);
        out = new PrintStream(screenOutput);
        System.setIn(in);
        System.setOut(out);
        isInit = true;
    }

    public void exit(){
        isInit = false;
        Console.clearConsole();
        System.setIn(oldIn);
        System.setOut(oldOut);
    }
}
