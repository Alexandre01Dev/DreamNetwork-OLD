package be.alexandre01.dreamzon.network.utils.screen.stream;

import be.alexandre01.dreamzon.network.utils.screen.Screen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScreenInput extends ByteArrayInputStream {
    public InputStream inputStream;

    public ScreenInput(byte[] buf) {
        super(buf);
    }


}
