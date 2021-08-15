package de.eldritch.TurtleFly.feature.boop;

import de.eldritch.TurtleFly.feature.Callable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractionBoop implements Callable {
    public InteractionBoop() {

    }

    /**
     * @see Callable#onEvent(Event)
     */
    public void onEvent(Event e) throws ClassCastException {
        PlayerInteractEntityEvent event = (PlayerInteractEntityEvent) e;

        if (event.getPlayer().getInventory().getItem(event.getHand()).getType().isAir()) {
            this.onInteraction(event.getRightClicked());
        }
    }

    /**
     * Called by {@link this#onEvent(Event)}
     *
     * @param entity Interaction target
     */
    private void onInteraction(Entity entity) {
        Player target = (Player) entity;

        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cBoop"));
        target.getWorld().spawnParticle(Particle.HEART, target.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5);
    }
}
