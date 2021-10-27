package de.eldritch.TurtleFly.module.chat;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatModule extends PluginModule implements Listener {
    public ChatModule() throws PluginModuleEnableException {
        super();

        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(this, TurtleFly.getPlugin());
    }

    /**
     * Reformats chat messages
     */
    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        // remove '@' reply prefix
        if (event.getMessage().startsWith("@")) {
            String[] tokens = event.getMessage().split(" ");
            try {
                Long.parseLong(tokens[0].substring(1));

                // remove prefix from message
                event.setMessage(event.getMessage().substring(tokens[0].length() + " ".length()));
            } catch (NumberFormatException ignored) {}
        }

        event.setFormat(ChatColor.GOLD + event.getPlayer().getDisplayName() + ChatColor.DARK_GRAY + ":  " + ChatColor.GRAY + event.getMessage());
    }
}
