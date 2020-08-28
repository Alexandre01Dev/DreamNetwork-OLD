package be.alexandre01.dreamzon.network.utils.screen;

import java.io.OutputStream;
import java.io.PrintStream;

public class Screen extends Thread {
    Process process;

    public Screen(Process process){
        this.process = process;
    }

    @Override
    public void run() {
        System.out.println();

        System.setIn(this.process.getInputStream());
        PrintStream pr = new PrintStream(this.process.getOutputStream());
        System.setOut(pr);

        System.out.println("it works");
    }
}
