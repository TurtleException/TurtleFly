package de.eldritch.TurtleFly.module.chat;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import de.eldritch.TurtleFly.module.chat.listeners.DiscordListener;
import de.eldritch.TurtleFly.module.chat.listeners.MinecraftListener;

import java.util.Objects;

public class ChatModule extends PluginModule {
    public ChatModule() throws PluginModuleEnableException {
        super();

        try {
            this.registerListeners();
        } catch (NullPointerException e) {
            throw new PluginModuleEnableException("Unable to register listeners.", e);
        }
    }

    private void registerListeners() throws NullPointerException {
        // minecraft listener
        Plugin.getPlugin().getServer().getPluginManager().registerEvents(new MinecraftListener(this), Plugin.getPlugin());

        // discord listener
        Objects.requireNonNull(Plugin.getPlugin().getDiscordAPI()).getJDA().addEventListener(new DiscordListener(this));
    }


    public void process(DiscordMessage msg) {
        Plugin.getPlugin().getServer().dispatchCommand(Plugin.getPlugin().getServer().getConsoleSender(), msg.toMinecraft());
    }

    public void process(MinecraftMessage msg) {
        if (Plugin.getPlugin().getDiscordAPI() != null) {
            // TODO: handle response prefix

            Plugin.getPlugin().getDiscordAPI().getMainTextChannel().sendMessage(msg.toDiscord()).queue();
        } else {
            Plugin.getPlugin().getLogger().warning("Unable to send discord message '" + msg.toDiscord() + "'.");
        }
    }
}
