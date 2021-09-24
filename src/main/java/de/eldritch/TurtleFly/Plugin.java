package de.eldritch.TurtleFly;

import de.eldritch.TurtleFly.module.ModuleManager;
import de.eldritch.TurtleFly.module.PluginModule;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    private static Plugin singleton;

    private ModuleManager moduleManager;


    @Override
    public void onEnable() {
        singleton = this;

        moduleManager = new ModuleManager();
    }

    @Override
    public void onDisable() {
        // save module YAML configs
        moduleManager.getRegisteredModules().forEach(PluginModule::saveConfig);
    }


    /**
     * @return Plugin singleton.
     */
    public static Plugin getPlugin() {
        return singleton;
    }
}
