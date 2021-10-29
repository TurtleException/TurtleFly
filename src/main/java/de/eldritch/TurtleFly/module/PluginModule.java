package de.eldritch.TurtleFly.module;

import de.eldritch.TurtleFly.TurtleFly;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * TODO
 */
public abstract class PluginModule {
    private final String moduleName = this.getClass().getSimpleName().substring(0, this.getClass().getSimpleName().length() - "Module".length()).toLowerCase();
    private final YamlConfiguration CONFIG = new YamlConfiguration();
    private final File FILE;

    // assign FILE
    {
        FILE = new File(TurtleFly.getPlugin().getDataFolder().getPath() + File.separator + "modules" + File.separator + moduleName + ".yml");
        if (!FILE.exists()) {
            TurtleFly.getPlugin().getLogger().info("Module '" + moduleName + "' config does not exist. Creating new file...");
            try {
                if (FILE.createNewFile())
                    TurtleFly.getPlugin().getLogger().info("File created!");
            } catch (IOException e) {
                throw new PluginModuleEnableException("Unable to create module config file!", e);
            }
        }
    }


    public PluginModule() throws PluginModuleEnableException {
        this.loadConfig();
    }


    /**
     * TODO
     * @throws PluginModuleEnableException
     */
    private void loadConfig() throws PluginModuleEnableException {
        try {
            CONFIG.load(FILE);
        } catch (IOException | InvalidConfigurationException e) {
            throw new PluginModuleEnableException("Unable to load module config for module '" + moduleName +"'.", e);
        }
    }

    /**
     * TODO
     */
    public void saveConfig() {
        try {
            CONFIG.save(FILE);
        } catch (IOException e) {
            TurtleFly.getPlugin().getLogger().log(Level.WARNING, "Unable to save config file for module '" + moduleName + "'.", e);
        }
    }

    /**
     * TODO
     */
    public void reloadConfig() {
        try {
            CONFIG.load(FILE);
        } catch (IOException | InvalidConfigurationException ignored) {}
    }

    public YamlConfiguration getConfig() {
        return CONFIG;
    }
}
