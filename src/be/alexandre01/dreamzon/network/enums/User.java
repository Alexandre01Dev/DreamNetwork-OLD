package be.alexandre01.dreamzon.network.enums;

import be.alexandre01.dreamzon.network.utils.Crypter;
import org.bukkit.permissions.Permission;

import java.util.Enumeration;

public enum User {
    Console("Console","8HetY4474XisrZ2FGwV5z" ,Permissions.ADMIN);

    String name;
    String password;
    Permissions permissions;

    User(String name, String password, Permissions permissions){
        this.name = name;
        this.password = password;
        this.permissions = permissions;
    }
    public boolean hasPermissions(Permissions permissions){
        if(permissions == this.permissions){
            return true;
        }
        return false;
    }
    public String getUsername(){
        return name;
    }
    public String getPassword(){
        return password;
    }
}
