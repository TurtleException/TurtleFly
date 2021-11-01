package de.eldritch.TurtleFly.module.sync.listeners;

import de.eldritch.TurtleFly.TurtleFly;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinecraftJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ComponentBuilder builder = new ComponentBuilder(TurtleFly.getChatPrefix());

        event.getPlayer().spigot().sendMessage(
                TurtleFly.getPlugin().getDiscordAPI() != null && TurtleFly.getPlugin().getDiscordAPI().getJDA() != null
                        ? builder.append("Discord-Chat synchronisiert.").create()
                        : builder.append("Discord-Chat nicht synchronisiert.").create());
    }
}
