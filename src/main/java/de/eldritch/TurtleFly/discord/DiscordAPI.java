package de.eldritch.TurtleFly.discord;

import de.eldritch.TurtleFly.TurtleFly;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Guild;

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

        this.setPresence();
        this.savePresence();
    }

    /**
     * Retrieve all information for {@link JDA#getPresence()#setPresence()}
     * from the plugin config and pass it to the {@link JDA}.
     *
     * @see DiscordAPI#savePresence()
     */
    public void setPresence() {
        String strStatus  = TurtleFly.getPlugin().getConfig().getString("discord.presence.status");
        String strActType = TurtleFly.getPlugin().getConfig().getString("discord.presence.activity.type");
        String strActName = TurtleFly.getPlugin().getConfig().getString("discord.presence.activity.name", "Minecraft");
        String strActUrl  = TurtleFly.getPlugin().getConfig().getString("discord.presence.activity.url", "https://eldritch.de");

        OnlineStatus status = (OnlineStatus.fromKey(strStatus) != null) ? OnlineStatus.fromKey(strStatus) : OnlineStatus.ONLINE;

        ActivityType actType;
        try {
            actType = ActivityType.valueOf(strActType);
        } catch (IllegalArgumentException e) {
            actType = ActivityType.DEFAULT;
        }

        API.getPresence().setPresence(status, actType.equals(ActivityType.STREAMING)
                ? Activity.streaming(strActName, strActUrl)
                : Activity.of(actType, strActName));
    }

    /**
     * Save all information from {@link JDA#getPresence()} to the plugin config.
     *
     * @see DiscordAPI#setPresence()
     */
    public void savePresence() {
        TurtleFly.getPlugin().getConfig().set("discord.presence.status", API.getPresence().getStatus().getKey());

        if (API.getPresence().getActivity() != null) {
            TurtleFly.getPlugin().getConfig().set("discord.presence.activity.type", API.getPresence().getActivity().getType().name());
            TurtleFly.getPlugin().getConfig().set("discord.presence.activity.name", API.getPresence().getActivity().getName());
            TurtleFly.getPlugin().getConfig().set("discord.presence.activity.url", API.getPresence().getActivity().getUrl());
        } else {
            TurtleFly.getPlugin().getConfig().set("discord.presence.activity.type", ActivityType.DEFAULT.name());
            TurtleFly.getPlugin().getConfig().set("discord.presence.activity.name", "Minecraft");
            TurtleFly.getPlugin().getConfig().set("discord.presence.activity.url", "https://eldritch.de");
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
