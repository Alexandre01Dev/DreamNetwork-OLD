package be.alexandre01.dreamzon.network.connection;

import java.net.Socket;
public class Remote {
    protected enum Type{
        Client,Spigot,Proxy
    }
    private Type type;

    private String processName;

    protected Remote(Type type){
        this.type = type;
        }

public Type getType() {
        return type;
        }

protected void setProcessName(String processName) {
        this.processName = processName;
        }

public String getProcessName(){
        return processName;
        }
        }
