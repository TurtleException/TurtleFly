package de.eldritch.TurtleFly.module.chat.listeners;

import de.eldritch.TurtleFly.module.chat.ChatModule;
import de.eldritch.TurtleFly.module.chat.MinecraftMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MinecraftListener implements Listener {
    private ChatModule module;

    public MinecraftListener(ChatModule module) {
        this.module = module;
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        MinecraftMessage minecraftMessage = new MinecraftMessage(event.getMessage(), event.getPlayer());

        module.process(minecraftMessage);
    }
}
