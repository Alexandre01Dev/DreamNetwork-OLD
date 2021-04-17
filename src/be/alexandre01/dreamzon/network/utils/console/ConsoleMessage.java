package be.alexandre01.dreamzon.network.utils.console;

import java.util.logging.Level;

public class ConsoleMessage {
    public String content;
    public Level level;

    public ConsoleMessage(String content, Level level) {
        this.content = content;
        this.level = level;
    }
}
