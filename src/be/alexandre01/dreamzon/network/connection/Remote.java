package be.alexandre01.dreamzon.network.connection;

import java.net.Socket;

public class Remote {
    protected enum Type{
        Client,Spigot,Proxy
    }
    private Type type;
    private Socket socket;
    private String processName;

    protected Remote(Type type, Socket socket){
        this.type = type;
        this.socket = socket;
    }

    public Type getType() {
        return type;
    }

    public Socket getSocket() {
        return socket;
    }

    protected void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessName(){
        return processName;
    }
}
