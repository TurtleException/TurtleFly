package de.eldritch.TurtleFly;

import de.eldritch.TurtleFly.discord.DiscordAPI;
import de.eldritch.TurtleFly.discord.DiscordConnectionException;
import de.eldritch.TurtleFly.module.ModuleManager;
import de.eldritch.TurtleFly.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class Plugin extends JavaPlugin {
    private static Plugin singleton;

    private DiscordAPI discordAPI;

    private ModuleManager moduleManager;


    @Override
    public void onEnable() {
        singleton = this;

        try {
            discordAPI = new DiscordAPI();
        } catch (DiscordConnectionException e) {
            getLogger().log(Level.WARNING, "Unable to instantiate DiscordAPI.", e);
        }

        moduleManager = new ModuleManager();
    }

    @Override
    public void onDisable() {
        // save module YAML configs
        moduleManager.getRegisteredModules().forEach(PluginModule::saveConfig);
    }


    /**
     * @return Discord API
     */
    public @Nullable DiscordAPI getDiscordAPI() {
        return discordAPI;
    }


    /**
     * @return Plugin singleton.
     */
    public static Plugin getPlugin() {
        return singleton;
    }
}
