package de.eldritch.TurtleFly.module.sync.listeners;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.sync.SyncModule;
import de.eldritch.TurtleFly.module.sync.DiscordMessage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    private SyncModule module;

    public DiscordListener(SyncModule module) {
        this.module = module;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) return; // abort if event was self-triggered

        TurtleFly.getPlugin().getLogger().info("[DISCORD] " + event.getAuthor().getDiscriminator() + ":  " + event.getMessage().getContentRaw());

        DiscordMessage discordMessage = new DiscordMessage(event.getMessage(), event.getMember());
        module.process(discordMessage);
    }
}
