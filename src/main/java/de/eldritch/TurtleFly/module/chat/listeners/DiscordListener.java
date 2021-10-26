package de.eldritch.TurtleFly.module.chat.listeners;

import de.eldritch.TurtleFly.module.chat.ChatModule;
import de.eldritch.TurtleFly.module.chat.DiscordMessage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {
    private ChatModule module;

    public DiscordListener(ChatModule module) {
        this.module = module;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        DiscordMessage discordMessage = new DiscordMessage(event.getMessage(), event.getMember());

        module.process(discordMessage);
    }
}
