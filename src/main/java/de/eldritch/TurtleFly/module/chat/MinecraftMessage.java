package de.eldritch.TurtleFly.module.chat;

import org.bukkit.entity.Player;

public class MinecraftMessage {
    private String message;
    private Player author;

    public MinecraftMessage(String message, Player author) {
        this.message = message;
        this.author = author;
    }

    /**
     * Formats the message to be compatible with
     * the Discord chat.
     */
    public String toDiscord() {
        String raw = message;
        String[] tokens = raw.split(" ");

        // remove response-prefix from actual message
        if (raw.startsWith("@") && tokens.length > 0)
            raw = raw.substring(tokens[0].length() + " ".length());

        String str = "__**" + author.getDisplayName() + "**:__  " + raw;

        if (str.length() >= 2000)
            str = str.substring(0, 1996) + "...";

        // add response-prefix to return String
        if (message.startsWith("@") && tokens.length > 0)
            str = tokens[0] + " " + str;

        return str;
    }
}
