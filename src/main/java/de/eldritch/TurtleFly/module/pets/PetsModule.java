package de.eldritch.TurtleFly.module.pets;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/**
 * Allows players to share pets.
 */
public class PetsModule extends PluginModule {
    public PetsModule() throws PluginModuleEnableException {
        super();

        this.registerListeners();
    }

    private void registerListeners() {
        Plugin.getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractAtEntityEvent event) {
                if (event.getRightClicked() instanceof Tameable) {
                    onClick(event.getPlayer(), (Tameable) event.getRightClicked());
                }
            }
        }, Plugin.getPlugin());
    }

    public void onClick(Player source, Tameable pet) {
        // TODO
    }
}
