package de.eldritch.TurtleFly;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private static Plugin singleton;


    @Override
    public void onEnable() {
        singleton = this;
    }

    @Override
    public void onDisable() {

    }


    /**
     * @return Plugin singleton.
     */
    public static Plugin getPlugin() {
        return singleton;
    }
}
