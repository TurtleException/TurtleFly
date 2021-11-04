package de.eldritch.TurtleFly.module.update;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UpdateCommand implements CommandExecutor {
    private final UpdateModule MODULE;

    public UpdateCommand(UpdateModule module) {
        this.MODULE = module;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        MODULE.update();
        return true;
    }
}
