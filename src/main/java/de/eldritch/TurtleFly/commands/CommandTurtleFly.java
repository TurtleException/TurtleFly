package de.eldritch.TurtleFly.commands;

import de.eldritch.TurtleFly.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTurtleFly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2)
            return false;

        return Plugin.getPlugin().getCallManager().process(sender, args);
    }
}
