package be.alexandre01.dreamzon.network.utils.screen;

import be.alexandre01.dreamzon.network.objects.Server;
import be.alexandre01.dreamzon.network.utils.screen.stream.ScreenStream;

import java.io.*;
import java.util.ArrayList;

public class  Screen extends Thread {
    Server server;
    ArrayList<String> history;
    ScreenStream screenStream;
    public Screen(Server server){
        this.server = server;
        this.history = new ArrayList<>();
        this.screenStream = new ScreenStream();
        ScreenManager.instance.addScreen(this);

    }

    @Override
    public void run() {
        System.out.println("yes");
    //    System.setIn(this.process.getInputStream());
        //PrintStream pr = new PrintStream(this.server.getProcessus().getOutputStream());
       // System.setOut(pr);
        String data = null;
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
