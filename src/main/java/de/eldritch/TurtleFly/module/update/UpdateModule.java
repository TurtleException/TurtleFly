package de.eldritch.TurtleFly.module.update;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import de.eldritch.TurtleFly.util.IllegalVersionException;
import de.eldritch.TurtleFly.util.Version;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class UpdateModule extends PluginModule {
    public UpdateModule() throws PluginModuleEnableException {
        super();

        this.updateConfigVersion();
        this.registerCommands();
    }

    private void updateConfigVersion() {
        String legacyFile = this.getConfig().getString("legacyFile");

        if (legacyFile != null) {
            File file = new File(legacyFile);
            TurtleFly.getPlugin().getLogger().info((file.delete() ? "Successfully deleted" : "Unable to delete") +  " legacy file '" + legacyFile + "'.");
        }
    }

    private void registerCommands() {
        try {
            Objects.requireNonNull(TurtleFly.getPlugin().getCommand("update")).setExecutor(new UpdateCommand(this));
        } catch (NullPointerException e) {
            TurtleFly.getPlugin().getLogger().warning("Unable to register command '/update'.");
        }
    }

    public void update() {
        File[] pluginFiles = TurtleFly.getPlugin().getDataFolder().getParentFile().listFiles(
                (dir, name) -> name.endsWith(".jar") && name.contains(TurtleFly.class.getSimpleName()));

        if (pluginFiles == null) {
            TurtleFly.getPlugin().getLogger().info("Unable to update version. (There is no file with a newer version)");
            return;
        }

        for (File file : pluginFiles) {
            try {
                Version fileVersion = Version.parseFromFileName(file.getName());
                if (TurtleFly.getPlugin().getVersion().isMoreRecent(fileVersion)) {
                    this.updateFromFile(file);
                    return;
                }
            } catch (IllegalVersionException e) {
                e.printStackTrace();
            }
        }

        TurtleFly.getPlugin().getLogger().info("Unable to update version. (There is no file with a newer version)");
    }

    private void updateFromFile(File file) {
        TurtleFly.getPlugin().getLogger().info("Attempting to load new plugin version...");
        Plugin plugin;
        try {
            plugin = TurtleFly.getPlugin().getServer().getPluginManager().loadPlugin(file);
            if (plugin == null) throw new NullPointerException("Plugin file cannot be null.");
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            TurtleFly.getPlugin().getLogger().log(Level.WARNING, "Unable to load new plugin version! Aborting...", e);
            return;
        }

        TurtleFly.getPlugin().getLogger().info("* * * * *");
        TurtleFly.getPlugin().getLogger().info("This version will be disabled and unloaded.");
        TurtleFly.getPlugin().getLogger().info("Should the new version fail to load, the plugin has");
        TurtleFly.getPlugin().getLogger().info("to be reloaded manually by restarting the server.");
        TurtleFly.getPlugin().getLogger().info("* * * * *");

        TurtleFly.getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(TurtleFly.getPlugin(), () -> {
            TurtleFly.getPlugin().getConfig().set("legacyFile", TurtleFly.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            TurtleFly.getPlugin().getServer().getPluginManager().disablePlugin(TurtleFly.getPlugin());
            TurtleFly.getPlugin().getServer().getPluginManager().enablePlugin(plugin);
        }, 100L);
    }
}
