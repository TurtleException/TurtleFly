package de.eldritch.TurtleFly.discord;

import de.eldritch.TurtleFly.TurtleFly;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;

public class DiscordAPI {
    private final JDA API;

    private final Guild GUILD;

    public DiscordAPI() throws DiscordConnectionException {
        try {
            // instantiate JDA
            API = JDABuilder.createDefault(TurtleFly.getPlugin().getConfig().getString("discord.token", "null")).build();
            API.awaitReady();

            // assign constants
            GUILD = API.getGuildById(Objects.requireNonNull(TurtleFly.getPlugin().getConfig().getString("discord.guild")));
        } catch (Exception e) {
            throw new DiscordConnectionException("Unable to instantiate JDA!", e);
        }
    }

    /**
     * Provides the {@link JDA} isntance.
     */
    public JDA getJDA() {
        return API;
    }

    /**
     * Provides the Discord {@link Guild}.
     */
    public Guild getGuild() {
        return GUILD;
    }
}
