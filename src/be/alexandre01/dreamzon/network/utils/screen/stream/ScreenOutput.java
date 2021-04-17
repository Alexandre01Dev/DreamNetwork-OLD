package be.alexandre01.dreamzon.network.utils.screen.stream;

import java.io.*;

public class ScreenOutput extends PrintStream {
    public ScreenOutput(OutputStream out) {
        super(out);
    }

}
