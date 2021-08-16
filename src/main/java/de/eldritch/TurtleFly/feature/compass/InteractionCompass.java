package de.eldritch.TurtleFly.feature.compass;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.feature.Callable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.io.File;
import java.io.IOException;

/**
 * Allows players to set their compass target to another
 * player. The target updates twice every second.
 */
public class InteractionCompass implements Callable {
    private File yamlFile = null;
    private final YamlConfiguration compassTargets = new YamlConfiguration();

    public InteractionCompass() {
        this.loadYaml();
    }

    /**
     * Loads the compassTarget config from file
     * "compassTargets.yml" in Plugin DataFolder.
     */
    private void loadYaml() {
        File[] files = Plugin.getPlugin().getDataFolder().listFiles();

        for (File file : files != null ? files : new File[0]) {
            if (file.getName().equals("compassTargets.yml")) {
                try {
                    compassTargets.load(file);
                    this.yamlFile = file;
                    return;
                } catch (IOException | InvalidConfigurationException ignored) {}
            }
        }
    }

    /** Saves the compassTarget config to file
     * "compassTargets.yml" in Plugin DataFolder.
     */
    private void saveYaml() {
        try {
            compassTargets.save(yamlFile);
        } catch (IOException e) {
            Plugin.getPlugin().getLogger().warning("Unable to save compass targets!");
            e.printStackTrace();
        }
    }

    /**
     * @see Callable#onEvent(Event)
     */
    @Override
    public void onEvent(Event e) throws ClassCastException {
        PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) e;

        Entity target = event.getRightClicked();

        if (target instanceof Player)
            this.onInteraction(event.getPlayer(), (Player) target);
    }

    /**
     * @see Callable#onCommand(CommandSender, String[])
     */
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 3)
            return false;

        if (args[2].equals("unbind") && sender instanceof Player) {
            return this.removeLink((Player) sender);
        } else {
            return false;
        }
    }

    private void onInteraction(Player player, Player target) {
        this.overwrite(player);

        Runnable runnable = () -> {
            if (player.getWorld().equals(target.getWorld())) {
                player.setCompassTarget(target.getLocation());
            }
        };
        int taskId = Plugin.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Plugin.getPlugin(), runnable, 0, 10);

        compassTargets.set(player.getUniqueId() + ".target", target.getUniqueId());
        compassTargets.set(player.getUniqueId() + ".id", taskId);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§e" + target.getDisplayName() + " ist jetzt dein Leitstein <3"));
    }

    /**
     * Overwrites / removes the former compassTarget of
     * a given player.
     * @param player The player in question
     */
    private void overwrite(Player player) {
        if (compassTargets.contains(player.getUniqueId().toString())) {
            int taskId = compassTargets.getInt(player.getUniqueId() + ".id");
            Plugin.getPlugin().getServer().getScheduler().cancelTask(taskId);

            /*
            The former config does not have to be removed as this method is exclusively called
            by this#onInteraction(Player, Player), which sets a new target and taskId.
             */
        }
    }

    /**
     * Remove the link of a player, if any exist.
     * @param player The player in question
     * @return wether there was a link to remove
     */
    private boolean removeLink(Player player) {
        if (compassTargets.contains(player.getUniqueId().toString())) {
            int taskId = compassTargets.getInt(player.getUniqueId() + ".id");
            Plugin.getPlugin().getServer().getScheduler().cancelTask(taskId);

            compassTargets.set(player.getUniqueId().toString(), null);

            this.saveYaml();

            return true;
        } else {
            // player has no link
            return false;
        }
    }
}
