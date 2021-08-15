package de.eldritch.TurtleFly.feature.wolfi;

import de.eldritch.TurtleFly.feature.Callable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Objects;

public class InteractionWolf implements Callable {
    public InteractionWolf() {

    }

    /**
     * @see Callable#onEvent(Event)
     */
    public void onEvent(Event e) throws ClassCastException {
        PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) e;


        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (player.getInventory().getItem(event.getHand()).getType().isAir())
            if (entity instanceof Wolf)
                this.onInteract(player, (Wolf) entity);
    }

    /**
     * Called by {@link this#onEvent(Event)}
     *
     * @param player Interaction source
     * @param wolf Interaction target
     */
    private void onInteract(Player player, Wolf wolf) {
        if (!wolf.isAdult()) {
            try {
                if (Objects.requireNonNull(wolf.getOwner()).getUniqueId().equals(player.getUniqueId())) {
                    return;
                }
            } catch (NullPointerException var7) {
                var7.printStackTrace();
            }

            wolf.setOwner(player);
            wolf.setSitting(!wolf.isSitting());
            if (!wolf.isSitting()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eWolfi wird jetzt dir folgen."));
            }

        }
    }
}
