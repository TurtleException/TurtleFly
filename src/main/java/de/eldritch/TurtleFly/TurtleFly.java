package de.eldritch.TurtleFly;

import de.eldritch.TurtleFly.discord.DiscordAPI;
import de.eldritch.TurtleFly.discord.DiscordConnectionException;
import de.eldritch.TurtleFly.module.ModuleManager;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.status.StatusModule;
import de.eldritch.TurtleFly.util.Performance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class TurtleFly extends JavaPlugin {
    private static TurtleFly singleton;
    private String serverName;

    private DiscordAPI discordAPI;

    private ModuleManager moduleManager;


    @Override
    public void onEnable() {
        singleton = this;

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Performance(), 100L, 1L);

        // update server name
        try {
            Properties serverProperties = new Properties();
            File propertiesFile = new File(getDataFolder().getParentFile().getParentFile(), "server.properties");
            serverProperties.load(new FileReader(propertiesFile));
            serverName = (String) serverProperties.getOrDefault("server-name", "null");
        } catch (IOException e) {
            getLogger().warning("Unable to read server.properties!");
            serverName = "null";
        }


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
        // update server status (StatusModule)
        moduleManager.getRegisteredModules().forEach(pluginModule -> {
            if (pluginModule.getClass().equals(StatusModule.class))
                ((StatusModule) pluginModule).offlineUpdate();
        });

        // save module YAML configs
        moduleManager.getRegisteredModules().forEach(PluginModule::saveConfig);

        discordAPI.getJDA().shutdown();
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

    /**
     * @return The <code>server-name</code> specified in <code>server.properties</code>.
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @return Default plugin chat prefix.
     */
    public static TextComponent getChatPrefix() {
        ComponentBuilder builder = new ComponentBuilder();
        builder.append("[")
                .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .append("SERVER")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .append("] ")
                .color(net.md_5.bungee.api.ChatColor.DARK_GRAY);

        TextComponent component = new TextComponent(builder.create());
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(ChatColor.GREEN + "TurtleFly\n" + ChatColor.ITALIC.toString() + ChatColor.GRAY.toString() + "v" + getPlugin().getDescription().getVersion())
        ));

        return new TextComponent(component);
    }
}
