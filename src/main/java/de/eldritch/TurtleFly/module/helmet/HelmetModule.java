package de.eldritch.TurtleFly.module.helmet;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Allows players to wear certain items as hats.
 */
public class HelmetModule extends PluginModule {
    private final MaterialContainer materialContainer = new MaterialContainer();


    public HelmetModule() throws PluginModuleEnableException {
        super();

        this.registerListeners();
    }


    private void registerListeners() {
        Plugin.getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractEntityEvent event) {
                if (event.getRightClicked() instanceof Player)
                    onInteraction(event.getPlayer(), (Player) event.getRightClicked(), event.getHand());
            }
        }, Plugin.getPlugin());
    }

    private void onInteraction(Player source, Player target, EquipmentSlot slot) {
        // TODO
    }
}
