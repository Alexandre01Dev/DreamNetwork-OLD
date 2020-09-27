package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.Main;
import be.alexandre01.dreamzon.network.utils.*;
import be.alexandre01.dreamzon.network.utils.console.Console;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;
import be.alexandre01.dreamzon.network.utils.message.channels.MessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ReadData extends Thread{
    Client remote;

    public ReadData(Client remote){
        this.remote = remote;
    }

    public void  run() {
        while (!remote.getClient().isClosed()){
            try {
                InputStream in = remote.getClient().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String data = null;
                while((data = reader.readLine()) != null){
                    String decoded = BasicCrypter.decode(data);
                    if(remote.getClient().isBound()){
                        Message decryptedData = Message.createFromJsonString(BasicCrypter.decode(data));
                        if(decryptedData.contains("Channel")){
                            System.out.println("YEs");
                            String channelKey = decryptedData.getString("Channel");
                            MessageChannel messageChannel;
                            if(Utils.messageChannels.containsKey(channelKey)){
                                messageChannel = Utils.messageChannels.get(channelKey);
                            }else {
                                messageChannel = new MessageChannel(channelKey);
                            }
                            if(decryptedData.contains("DestroyChannel")){
                                if(decryptedData.getBoolean("DestroyChannel")){
                                    messageChannel.destroy();
                                }
                            }
                            messageChannel.receivedData(decryptedData);
                        }
                        if(decoded.contains("*")){
                            String[] splitter = decoded.split("\\*");
                            StringBuffer sb = new StringBuffer();

                            for (int i = 1; i < splitter.length; i++) {
                                sb.append(splitter[i]+" ");
                            }

                            remote.readData(Message.createFromJsonString(sb.toString()),splitter[0]);
                        }else {
                            remote.readData(  decryptedData,null);
                        }
                    }



                }
                remote.getClient().close();
            }catch (Exception e){
                Console.print("Remote failed: Error #2", Level.SEVERE);
                try{
                    String name = remote.getProcessName();
                    remote.getClient().close();
                    String pathName;
                    if(remote.isProxy()){
                        pathName = "proxy";


                    }else {
                        pathName = "server";
                        Main.getInstance().getProxy().sendData(new Message().set("STOP",remote.getProcessName()));
                        for(Client clients : Main.getInstance().getClients()){
                            clients.sendData(new Message().set("REMSERVERLIST",remote.getProcessName()));
                        }
                    }

                    if(!remote.isReload){
                        ServerInstance.stopServer(name,pathName);
                    }else{
                        //System.out.println(name);
                        new Connect("localhost",remote.getPort(),"Console","8HetY4474XisrZ2FGwV5z",name);
                    }

                    this.stop();
                }catch (Exception f){
                    Console.print("Remote failed: Error #3", Level.SEVERE);
                }
            }
        }
        //System.out.println("C'est down !");
        String name = remote.getProcessName();
        String pathName;
        boolean isReload = remote.isReload;
        if(remote.isProxy()){
            pathName = "proxy";


        }else {
        pathName = "server";
        pathName = "server";
        int port = remote.getPort();

        Main.getInstance().getProxy().sendData(new Message().set("STOP",remote.getProcessName()));
        for(Client clients : Main.getInstance().getClients()){
            clients.sendData(new Message().set("REMSERVERLIST",remote.getProcessName()));
        }
        Main.getInstance().getClients().remove(remote);
            try {
                remote.getClient().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            remote = null;

            if(!isReload){
                ServerInstance.stopServer(name,pathName);
            }else{
                //System.out.println(name);
               // System.out.println(port);
                new Connect("localhost",port,"Console","8HetY4474XisrZ2FGwV5z",name);
            }
    }

        this.stop();
    }
}
