package de.eldritch.TurtleFly.module.sync.listeners;

import de.eldritch.TurtleFly.TurtleFly;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinecraftJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ComponentBuilder builder = new ComponentBuilder(TurtleFly.getChatPrefix());

        builder.append((TurtleFly.getPlugin().getDiscordAPI() != null && TurtleFly.getPlugin().getDiscordAPI().getJDA() != null)
                ? "Discord-Chat synchronisiert."
                : "Discord-Chat nicht synchronisiert.")
                .color(ChatColor.GRAY);

        Bukkit.getScheduler().runTaskLaterAsynchronously(TurtleFly.getPlugin(), () ->
                event.getPlayer().spigot().sendMessage(builder.create()), 20L);
    }
}
