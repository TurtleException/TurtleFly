package de.eldritch.TurtleFly.module.click;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClickCommand implements CommandExecutor {
    private final ClickModule MODULE;

    public ClickCommand(ClickModule module) {
        this.MODULE = module;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        ClickGUIHandler.handle((Player) commandSender);
        return true;
    }
}
