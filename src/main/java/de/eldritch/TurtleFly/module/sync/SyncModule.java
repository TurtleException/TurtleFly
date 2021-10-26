package de.eldritch.TurtleFly.module.sync;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import de.eldritch.TurtleFly.module.sync.listeners.DiscordListener;
import de.eldritch.TurtleFly.module.sync.listeners.MinecraftListener;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Mirrors the Minecraft chat with Discord and provides custom formatting.
 */
public class SyncModule extends PluginModule {
    public SyncModule() throws PluginModuleEnableException {
        super();

        try {
            this.registerListeners();
        } catch (NullPointerException e) {
            throw new PluginModuleEnableException("Unable to register listeners.", e);
        }
    }

    private void registerListeners() throws NullPointerException {
        // minecraft listener
        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new MinecraftListener(this), TurtleFly.getPlugin());

        // discord listener
        Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI()).getJDA().addEventListener(new DiscordListener(this));
    }


    /**
     * Passes a Discord message to Minecraft.
     */
    public void process(DiscordMessage msg) {
        TurtleFly.getPlugin().getServer().spigot().broadcast(msg.toMinecraft());
    }

    /**
     * Passes a Minecraft message to Discord.
     */
    public void process(MinecraftMessage msg) {
        if (TurtleFly.getPlugin().getDiscordAPI() != null) {
            String msgStripped = msg.toDiscord();

            if (msgStripped.startsWith("@")) {
                String[] tokens = msgStripped.split(" ");
                if (tokens.length > 0) {
                    String responseTarget = tokens[0].substring(1);
                    try {
                        // get target message and create replay
                        Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI().getMainTextChannel().getHistory().getMessageById(responseTarget)).reply(msgStripped.substring(tokens[0].length() + "@ ".length())).queue();
                        return; // prevent the message from being sent seperately
                    } catch (NullPointerException e) {
                        TurtleFly.getPlugin().getLogger().warning("Unable to send discord message '" + msg.toDiscord() + "' as reply.");
                    }
                }
            }

            TurtleFly.getPlugin().getDiscordAPI().getMainTextChannel().sendMessage(msgStripped).queue();
        } else {
            TurtleFly.getPlugin().getLogger().warning("Unable to send Discord message '" + msg.toDiscord() + "'.");
        }
    }
}
