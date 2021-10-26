package de.eldritch.TurtleFly.module.helmet;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractEntityEvent event) {
                if (event.getRightClicked() instanceof Player
                        && materialContainer.hasMaterial(event.getPlayer().getInventory().getItem(event.getHand()).getType())) {
                    onClick(event.getPlayer(), (Player) event.getRightClicked(), event.getHand());
                }
            }
        }, TurtleFly.getPlugin());
    }

    public void onClick(Player source, Player target, EquipmentSlot slot) {
        target.getInventory().setHelmet(new ItemStack(source.getInventory().getItem(slot).getType(), 1));

        int amount = source.getInventory().getItem(slot).getAmount();
        if (amount == 1) {
            source.getInventory().remove(source.getInventory().getItem(slot));
        } else if (amount > 1) {
            source.getInventory().getItem(slot).setAmount(amount - 1);
        }
    }
}
