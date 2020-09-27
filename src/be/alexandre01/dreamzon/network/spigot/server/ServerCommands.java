package be.alexandre01.dreamzon.network.spigot.server;

import be.alexandre01.dreamzon.network.enums.Permissions;

public enum ServerCommands {
    BAN("ban", Permissions.ADMIN),
    OP("op",Permissions.ADMIN);


    String command;
    Permissions[] permissions;
    ServerCommands(String command, Permissions... permission){
        this.command = command;
        this.permissions = permission;
    }

    public String getCommand() {
        return command;
    }

    public Permissions[] getPermissions() {
        return permissions;
    }
}
