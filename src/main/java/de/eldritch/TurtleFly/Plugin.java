package de.eldritch.TurtleFly;

import de.eldritch.TurtleFly.feature.CallManager;
import de.eldritch.TurtleFly.listeners.ListenerPlayerInteractEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private static Plugin plugin;

    private final CallManager callManager = new CallManager();

    public Plugin() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new ListenerPlayerInteractEntity(), this);
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public CallManager getCallManager() {
        return callManager;
    }
}
