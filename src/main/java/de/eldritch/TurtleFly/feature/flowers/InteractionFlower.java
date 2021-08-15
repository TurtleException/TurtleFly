package de.eldritch.TurtleFly.feature.flowers;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.feature.Callable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InteractionFlower implements Callable {
    // all accepted materials
    private static final MaterialContainer materials = new MaterialContainer();

    /**
     * @see Callable#onEvent(Event)
     */
    public void onEvent(Event e) throws ClassCastException {
        PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) e;

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        EquipmentSlot slot = event.getHand();

        if (entity instanceof Player) {
            if (materials.hasMaterial(player.getInventory().getItem(slot).getType())) {
                this.onInteraction(player, entity, slot);
            }
        }
    }

    /**
     * Called by {@link this#onEvent(Event)}
     *
     * @param player Interaction source
     * @param entity Interaction target
     * @param slot Equipped items of source player
     */
    private void onInteraction(Player player, Entity entity, EquipmentSlot slot) {
        try {
            if (Objects.requireNonNull(((Player)entity).getPlayer()).getInventory().getHelmet() != null) {
                return;
            }

            Objects.requireNonNull(((Player)entity).getPlayer()).getInventory().setHelmet(new ItemStack(player.getInventory().getItem(slot).getType(), 1));
        } catch (NullPointerException e) {
            Plugin.getPlugin().getLogger().warning("This should not have happened. Somehow you reached code which is not designed to be reached. (Somehow a player has no inventory)");
            e.printStackTrace();
        }

        int amount = player.getInventory().getItem(slot).getAmount();
        if (amount == 1) {
            player.getInventory().remove(player.getInventory().getItem(slot));
        } else if (amount > 1) {
            player.getInventory().getItem(slot).setAmount(amount - 1);
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eDu hast " + Objects.requireNonNull(((Player)entity).getPlayer()).getDisplayName() + " eine Blume aufgesetzt."));
        Objects.requireNonNull(((Player)entity).getPlayer()).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§e" + player.getDisplayName() + " hat dir eine Blume aufgesetzt."));
    }
}
