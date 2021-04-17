package be.alexandre01.dreamzon.network.utils.screen.stream;

import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.console.ImpatientInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;

    public class ScreenInReader extends Thread {
        Console console;

        Server server;

        private boolean isRunning;

        public ScreenInReader(Console console, Server server) {
            this.console = console;
            this.server = server;
        }

        public void run() {
            Process process = this.server.getProcessus();
            InputStream in = process.getInputStream();
            this.isRunning = true;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
                String data = null;

                while ( (data = bufferedReader.readLine()) != null && bufferedReader.ready()) {
                    String dataNoSpace = data.replaceAll(" ", "");
                    for (int i = 0; i < dataNoSpace.length(); i++) {
                        if (dataNoSpace.charAt(i) != '>') {
                            this.console.fPrint(data, Level.FINE);
                            break;
                        }
                    }
                }
            } catch (Exception exception) {}
        }


}