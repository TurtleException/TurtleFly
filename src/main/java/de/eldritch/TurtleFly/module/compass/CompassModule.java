package de.eldritch.TurtleFly.module.compass;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Allows players to set compass targets on entities.
 */
public class CompassModule extends PluginModule {
    public CompassModule() throws PluginModuleEnableException {
        super();

        this.registerListeners();
    }


    private void registerListeners() {
        Plugin.getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractEntityEvent event) {
                if (event.getPlayer().getInventory().getItem(event.getHand()).getType().equals(Material.COMPASS)
                        && event.getRightClicked() instanceof Player) {
                    onClick(event.getPlayer(), (Player) event.getRightClicked());
                }
            }
        }, Plugin.getPlugin());

        // TODO: remove link on compass use
        // (possible solution: temp value of previous location)
    }

    public void onClick(Player source, @NotNull Player target) {
        this.cancelRunnable(source);

        Runnable runnable = () -> {
            if (source.getWorld().equals(target.getWorld())) {
                source.setCompassTarget(target.getLocation());
            }
        };
        int taskId = Plugin.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Plugin.getPlugin(), runnable, 0, 10);

        getConfig().set(target.getUniqueId() + ".target", target.getUniqueId());
        getConfig().set(target.getUniqueId() + ".id", taskId);
    }

    /**
     * Overwrites / removes the former compassTarget of
     * a given player.
     * @param player The player in question (source).
     */
    private void cancelRunnable(Player player) {
        if (getConfig().contains(player.getUniqueId().toString())) {
            int taskId = getConfig().getInt(player.getUniqueId() + ".id");
            Plugin.getPlugin().getServer().getScheduler().cancelTask(taskId);

            /*
            The former config does not have to be removed as this method is exclusively
            called by this#onClick, which sets a new target and taskId.
             */
        }
    }

    /**
     * Remove the link of a player, if any exist.
     * @param player The player in question.
     * @return wether there was a link to remove.
     */
    private boolean removeLink(Player player) {
        if (getConfig().contains(player.getUniqueId().toString())) {
            int taskId = getConfig().getInt(player.getUniqueId() + ".id");
            Plugin.getPlugin().getServer().getScheduler().cancelTask(taskId);

            getConfig().set(player.getUniqueId().toString(), null);

            this.saveConfig();

            return true;
        } else {
            // player has no link
            return false;
        }
    }
}
