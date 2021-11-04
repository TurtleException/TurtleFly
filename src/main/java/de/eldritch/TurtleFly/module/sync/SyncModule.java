package de.eldritch.TurtleFly.module.sync;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import de.eldritch.TurtleFly.module.sync.listeners.DiscordListener;
import de.eldritch.TurtleFly.module.sync.listeners.MinecraftJoinListener;
import de.eldritch.TurtleFly.module.sync.listeners.MinecraftListener;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;

/**
 * Mirrors the Minecraft chat with Discord and provides custom formatting.
 */
public class SyncModule extends PluginModule {
    public SyncModule() throws PluginModuleEnableException {
        super();

        if (TurtleFly.getPlugin().getDiscordAPI() == null)
            throw new PluginModuleEnableException("Module is dependant on JDA connection.");

        try {
            this.registerListeners();
        } catch (NullPointerException e) {
            throw new PluginModuleEnableException("Unable to register listeners.", e);
        }
    }

    private void registerListeners() throws NullPointerException {
        // minecraft listener
        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new MinecraftListener(this), TurtleFly.getPlugin());
        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new MinecraftJoinListener(), TurtleFly.getPlugin());

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
            if (msg.getReplyTarget() != null) {
                try {
                    // get target message and create replay
                    Objects.requireNonNull(this.getTextChannel().retrieveMessageById(msg.getReplyTarget())).complete().reply(msg.toDiscord()).queue();
                    return; // prevent the message from being sent seperately
                } catch (NullPointerException e) {
                    TurtleFly.getPlugin().getLogger().warning("Unable to send discord message '" + msg.toDiscord() + "' as reply.");
                    e.printStackTrace();
                }
            }

            this.getTextChannel().sendMessage(msg.toDiscord()).queue();
        } else {
            TurtleFly.getPlugin().getLogger().warning("Unable to send Discord message '" + msg.toDiscord() + "'.");
        }
    }

    private TextChannel getTextChannel() {
        return Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI()).getGuild().getTextChannelById(getConfig().getString("discord.textChannel", "null"));
    }
}
