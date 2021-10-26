package de.eldritch.TurtleFly;

import de.eldritch.TurtleFly.discord.DiscordAPI;
import de.eldritch.TurtleFly.discord.DiscordConnectionException;
import de.eldritch.TurtleFly.module.ModuleManager;
import de.eldritch.TurtleFly.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class TurtleFly extends JavaPlugin {
    private static TurtleFly singleton;

    private DiscordAPI discordAPI;

    private ModuleManager moduleManager;


    @Override
    public void onEnable() {
        singleton = this;


        // idk why spigot doesn't do that
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        try {
            new File(getDataFolder(), "config.yml").createNewFile();
            new File(getDataFolder(), "modules").mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
    public static TurtleFly getPlugin() {
        return singleton;
    }
}
