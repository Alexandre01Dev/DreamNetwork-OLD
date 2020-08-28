package be.alexandre01.dreamzon.network.utils.message.channels;

import be.alexandre01.dreamzon.network.connection.Remote;
import be.alexandre01.dreamzon.network.utils.Utils;
import be.alexandre01.dreamzon.network.utils.crypter.BasicCrypter;
import be.alexandre01.dreamzon.network.utils.message.Message;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageChannel {
    private String channel = "DreamNetwork";
    private Socket socket;
    private ArrayList<ReadChannel> reads;
    private String provider = null;
    private boolean isMultiThread = Utils.isMultiThread;
    private ArrayList<Remote> remotes;
    public MessageChannel(){
        System.out.println("CreateChannel");
        Message message = new Message().set("GetChannel",channel);
        remotes = new ArrayList<>();
        reads = new ArrayList<>();
        Utils.messageChannels.put(channel,this);
        this.socket = Utils.socket;
        sendData(message);
    }

    public MessageChannel(String channel){
        System.out.println("CreateChannel");
        Message message = new Message().set("GetChannel",channel);
        remotes = new ArrayList<>();
        reads = new ArrayList<>();
        Utils.messageChannels.put(channel,this);
        this.channel = channel;
        this.socket = Utils.socket;
        sendData(message);
    }
    public MessageChannel(String channel,Socket socket){
        Message message = new Message().set("GetChannel",channel);
        remotes = new ArrayList<>();
        reads = new ArrayList<>();
        Utils.messageChannels.put(channel,this);
        this.channel = channel;
        this.socket = socket;
        sendData(message);

    }
    public void sendData(Message data){
        data.set("Channel",channel);
        if(provider != null) data.set("Provider",provider);

        String sData = BasicCrypter.encode(data.toString());

        try{
            OutputStream out = socket.getOutputStream();

            PrintWriter writer = new PrintWriter(out);
            writer.write(sData+"\n");
            writer.flush();
        }catch (Exception e){
            System.out.println("FAIL #7");
        }

    }
    public void addRemote(Remote remote){
        remotes.add(remote);
    }

    public void removeRemote(Remote remote){
        remotes.remove(remote);
    }
    public void setupActions(ReadChannel... readChannel){
        reads.addAll(Arrays.asList(readChannel));
        System.out.println(readChannel);
        System.out.println(reads);
    }
    public void receivedData(Message message){
        for (ReadChannel r : reads){
            System.out.println("ReadChannel" + channel);
            r.read(message,this);
        }
    }

    public void destroy(){
        sendData(new Message().set("DestroyChannel",true));
        Utils.messageChannels.remove(channel);
    }

    public interface ReadChannel{
        void read(Message message,MessageChannel channel);
    }
}
