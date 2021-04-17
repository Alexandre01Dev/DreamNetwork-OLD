package be.alexandre01.dreamzon.network.utils.screen;

import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.screen.stream.ScreenStream;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class  Screen extends Thread {
    Server server;
    ArrayList<String> history;
    ScreenStream screenStream;
    ScheduledExecutorService executors;
    public Screen(Server server){
        this.server = server;
        this.history = new ArrayList<>();
        this.screenStream = new ScreenStream();
        ScreenManager.instance.addScreen(this);
        executors = Executors.newScheduledThreadPool(16);

    }

    @Override
    public void run() {
        executors.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String data = null;
                Instant now = Instant.now();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(server.getProcessus().getInputStream()));
                try {
                    while (server.getProcessus().isAlive()){
                        if(Duration.between(now,Instant.now()).toMillis()<= 200){
                        while ((data = bufferedReader.readLine()) != null){
                            screenStream.out.println("KRRRRRRRRR");
                            if(history.size() > 25){
                                history.remove(0);
                            }
                            history.add(data);
                            if(screenStream.isInit)
                                screenStream.out.println(data);
                        }
                        }else {
                            //server.getProcessus().wait(1);
                            break;
                        }

                    }
                    destroy();
                }catch (Exception e){

                }


                //System.out.println("it works");
            }
        },0,5200, TimeUnit.MILLISECONDS);
        System.out.println("yes");
    //    System.setIn(this.process.getInputStream());
        //PrintStream pr = new PrintStream(this.server.getProcessus().getOutputStream());
       // System.setOut(pr);
       /* String data = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(server.getProcessus().getInputStream()));
            try {
            while (server.getProcessus().isAlive()){
               while ((data = bufferedReader.readLine()) != null){
                    if(history.size() > 25){
                        history.remove(0);
                    }
                    history.add(data);
                    if(screenStream.isInit)
                    screenStream.out.println(data);
                }
            }
            destroy();
        }catch (Exception e){

        }

*/
        System.out.println("it works");
    }

    public void destroy(){
        ScreenManager.instance.remScreen(this);
        screenStream.exit();
    }

    public Server getServer() {
        return server;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public ScreenStream getScreenStream() {
        return screenStream;
    }
}
