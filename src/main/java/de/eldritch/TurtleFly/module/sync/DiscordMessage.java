package de.eldritch.TurtleFly.module.sync;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class DiscordMessage {
    private Message message;
    private Member author;

    public DiscordMessage(Message message, Member author) {
        this.message = message;
        this.author = author;
    }

    /**
     * Formats the message to be compatible with
     * the Minecraft chat.
     */
    public String toMinecraft() {
        String authorColor;
        if (author.getUser().isBot() || author.getUser().isSystem())
            authorColor = "gray";
        else
            authorColor = "aqua";

        String hoverText;
        if (author.getColor() != null)
            hoverText = "{\"text\":\"" + author.getEffectiveName() + "\",\"color\":\"#" + String.format("#%02X%02X%02X", author.getColor().getRed(), author.getColor().getGreen(), author.getColor().getBlue()) + "\"}";
        else
            hoverText = "{\"text\":\"" + author.getEffectiveName() + "\",\"color\":\"aqua\"}";

        String content = message.getContentRaw();
        content = this.replaceMarkdown(content);
        content = this.replaceEmotes(content);

        String responsePrefix = "@" + message.getId() + " ";

        return "tellraw @a [\"\",{\"text\":\""
                + author.getNickname() + "\",\"bold\":true,\"color\":\""
                + authorColor + "\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\""
                + hoverText + "\"}},{\"text\":\": \\u0020\",\"color\":\"dark_gray\"},{\"text\":\""
                + content + "\",\"color\":\"gray\",\"insertion\":\"" + responsePrefix + "\"}]";
    }

    /**
     * Strips markdown characters like *, **, __, ~~, ||.
     * The reason {@link Message#getContentStripped()} is not used here is because
     * some markdown characters can still be formatted in Minecraft.
     */
    private String replaceMarkdown(String str) {
        return str; // useless for now
    }

    /**
     * Replaces certain Emotes with raw text versions like :) or :c
     */
    private String replaceEmotes(String str) {
        return str; // useless for now
    }
}
