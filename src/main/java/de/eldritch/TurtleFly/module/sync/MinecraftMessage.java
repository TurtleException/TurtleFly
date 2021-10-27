package de.eldritch.TurtleFly.module.sync;

import de.eldritch.TurtleFly.TurtleFly;
import net.dv8tion.jda.api.entities.Emote;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinecraftMessage {
    private String message;
    private Player author;

    private String replyTarget = null;

    public MinecraftMessage(String message, Player author) {
        this.message = message;
        this.author = author;


        String[] tokens = message.split(" ");

        // remove response-prefix from actual message
        if (message.startsWith("@") && tokens.length > 0) {
            this.message = message.substring(tokens[0].length() + " ".length());
            replyTarget = tokens[0].substring(1);
        }
    }

    /**
     * Formats the message to be compatible with
     * the Discord chat.
     */
    public String toDiscord() {
        String emote = "";
        if (TurtleFly.getPlugin().getDiscordAPI() != null) {
            List<Emote> emotes = TurtleFly.getPlugin().getDiscordAPI().getMainTextChannel().getGuild().getEmotesByName("minecraft", true);

            if (!emotes.isEmpty())
                emote = emotes.get(0).getAsMention() + " ";
        }

        String str = emote + "__**" + author.getDisplayName() + "**:__  " + message;

        if (str.length() >= 2000)
            str = str.substring(0, 1996) + "...";

        return str;
    }

    /**
     * @return Message id of the reply target, <code>null</code>
     *         if the message is not a reply.
     */
    public @Nullable String getReplyTarget() {
        return this.replyTarget;
    }
}
