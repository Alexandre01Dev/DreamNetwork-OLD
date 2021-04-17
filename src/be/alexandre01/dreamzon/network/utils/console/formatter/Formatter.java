package be.alexandre01.dreamzon.network.utils.console.formatter;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.utils.console.interceptor.Interceptor;
import be.alexandre01.dreamzon.network.utils.console.logging.LoggingOutputStream;
import org.fusesource.jansi.Ansi;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

public class Formatter {
    PrintStream defaultStream;
    public  PrintStream prStr;
    public void format(){
        Ansi.setEnabled(true);
        try {
            System.setProperty("file.encoding","UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null,null);
        }catch (Exception e){
            e.printStackTrace();
        }

        defaultStream = System.out;
        ByteArrayOutputStream loggerContent = new LoggingOutputStream(Main.getLogger(), Level.ALL);
        prStr = null;
        prStr = new Interceptor(loggerContent);

        StreamHandler streamHandler = new StreamHandler(prStr, new ConciseFormatter(false));

        final PrintStream err = System.err;
        Main.getLogger().setUseParentHandlers(false);
        try {
            ConciseFormatter c = new ConciseFormatter(false);
            ConsoleHandler handler = new ConsoleHandler();
            try {
                handler.setEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            handler.setFormatter(c);
            handler.flush();
            Main.getLogger().addHandler( handler);
            System.setOut(prStr);
            System.setErr(System.out);


        } finally {
            System.setErr(err);
        }
    }

    public PrintStream getDefaultStream() {
        return defaultStream;
    }

    public PrintStream getPrStr() {
        return prStr;
    }
}
