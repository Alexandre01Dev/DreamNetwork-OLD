package be.alexandre01.dreamzon.network.commands;

import com.sun.istack.internal.NotNull;

public interface CommandsExecutor {
    boolean onCommand(@NotNull String[] args);
}
