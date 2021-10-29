package de.eldritch.TurtleFly.module.emote;

import de.eldritch.TurtleFly.TurtleFly;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Icon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();

        if (TurtleFly.getPlugin().getDiscordAPI() == null)
            return;

        // remove emote
        for (Emote emote : TurtleFly.getPlugin().getDiscordAPI().getGuild().getEmotes())
            if (emote.getName().equals(name)) emote.delete().complete();

        // retrieve emote
        byte[] emote;
        try {
            emote = EmoteModule.retrieveAvatar(name);
        } catch (IOException e) {
            TurtleFly.getPlugin().getLogger().warning("Unable to retrieve emote '" + name + "'.");
            return;
        }

        TurtleFly.getPlugin().getDiscordAPI().getGuild().createEmote(name, Icon.from(emote)).queue(
                emote1 -> TurtleFly.getPlugin().getLogger().info("Emote '" + emote1.getName() + "' created."));
    }
}
