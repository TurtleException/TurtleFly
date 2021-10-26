package de.eldritch.TurtleFly.module.sync.listeners;

import de.eldritch.TurtleFly.module.sync.SyncModule;
import de.eldritch.TurtleFly.module.sync.MinecraftMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MinecraftListener implements Listener {
    private SyncModule module;

    public MinecraftListener(SyncModule module) {
        this.module = module;
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        MinecraftMessage minecraftMessage = new MinecraftMessage(event.getMessage(), event.getPlayer());
        module.process(minecraftMessage);
    }
}
