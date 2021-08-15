package de.eldritch.TurtleFly.listeners;

import de.eldritch.TurtleFly.Plugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ListenerPlayerInteractEntity implements Listener {
    @EventHandler
    public void onEvent(PlayerInteractEntityEvent event) {
        Plugin.getPlugin().getCallManager().process(event);
    }
}
