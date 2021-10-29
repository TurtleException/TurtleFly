package de.eldritch.TurtleFly.module.status;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.TimeFormat;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Regularily posts an embedded Message to a specific Discord
 * channel that provides some status information on the server.
 */
public class StatusModule extends PluginModule {
    public StatusModule() throws PluginModuleEnableException {
        super();

        if (TurtleFly.getPlugin().getDiscordAPI() == null)
            throw new PluginModuleEnableException("Module is dependant on JDA connection.");

        this.registerListeners();
        this.startThread();
    }

    private void registerListeners() {
        // null
    }

    private void startThread() {
        TimerThread thread = new TimerThread();
        thread.setName("StatusModule_TimerThread");
        thread.setDaemon(true);
        thread.start();
    }


    /**
     * Updates the status information and passes them on to
     * Discord via {@link StatusModule#updateMessage(MessageEmbed)}.
     *
     * @see TimerThread#run()
     */
    public void refresh() {
        reloadConfig();
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Server Status")
                .setDescription("Letztes Update: " + TimeFormat.RELATIVE.now() + ".\n "
                        + "Der Bot versucht diese Nachricht alle 5 Sekunden zu aktualisieren. "
                        + "Da je nach Auslastung der *Discord API* oder des Servers einige dieser "
                        + "Updates ausfallen könnten ist spätestens nach einer Minute damit zu "
                        + "rechnen, dass der Server offline ist oder das Plugin nicht funktioniert.")
                .setThumbnail(!TurtleFly.getPlugin().getServer().getName().equals("Unknown Server")
                        ? "http://cdn.eldritch.de/mc/EldritchDiscord/" + TurtleFly.getPlugin().getServer().getName() + ".png"
                        : "http://cdn.eldritch.de/mc/EldritchDiscord/unknown.png")
                .setColor(0x2F3136)
                .setFooter("turtlefly.eldritch.de")
                .setTimestamp(new Date().toInstant());

        // online players (per world)
        Map<String, Collection<Player>> players = this.getOnlinePlayers();
        if (players.isEmpty()) {
            builder.addField("Spieler", "Keine Spieler online.", true);
        } else {
            players.forEach((world, players1) -> {
                StringBuilder str = new StringBuilder();
                players1.forEach(player -> {
                    try {
                        str.append(Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI()).getGuild().getEmotesByName(player.getName(), false).get(0).getAsMention());
                    } catch (NullPointerException | IndexOutOfBoundsException ignored) {
                        str.append(":question:");
                    }
                    str.append(" ").append(player.getDisplayName()).append("\n");
                });

                // check for a custom world name in the config
                try {
                    if (Objects.requireNonNull(getConfig().getConfigurationSection("worlds")).contains(world)) {
                        builder.addField("Spieler | " + Objects.requireNonNull(getConfig().getConfigurationSection("worlds")).get(world), str.toString(), true);
                    }
                } catch (NullPointerException ignored) {
                    builder.addField("Spieler | " + world, str.toString(), true);
                }
            });
        }


        this.updateMessage(builder.build());
    }

    /**
     * Updates the Discord status embed message. If the message does not
     * already exist a new one is created.
     * @param embed The new embed version.
     */
    private void updateMessage(MessageEmbed embed) {
        // check wether API is online
        if (TurtleFly.getPlugin().getDiscordAPI() == null || TurtleFly.getPlugin().getDiscordAPI().getGuild() == null)
            return;

        TextChannel channel = TurtleFly.getPlugin().getDiscordAPI().getGuild().getTextChannelById(getConfig().getLong("discord.channel"));
        if (channel == null) return;

        channel.retrieveMessageById(getConfig().getLong("discord.message")).queue((message) -> {
            message.editMessageEmbeds(embed).queue();
        }, (failure) -> {
            // see https://stackoverflow.com/a/59805069 for further information on how to proceed here
            channel.sendMessageEmbeds(embed).queue(message -> {
                // save new message id
                getConfig().set("discord.message", message.getIdLong());
                saveConfig();
            });
        });
    }


    /**
     * Provides a {@link Collection} of all online {@link Player Players}
     * per world as a {@link Map}. Each key is the name of a world, the
     * values are {@link Collection Collections} of players in that world.
     */
    private Map<String, Collection<Player>> getOnlinePlayers() {
        Collection<? extends Player> players = TurtleFly.getPlugin().getServer().getOnlinePlayers();
        Map<String, Collection<Player>> playerMap = new HashMap<>();

        // organize players by world name
        for (Player player : players) {
            if (playerMap.containsKey(player.getWorld().getName())) {
                playerMap.get(player.getWorld().getName()).add(player);
            } else {
                HashSet<Player> worldPlayers = new HashSet<>();
                worldPlayers.add(player);
                playerMap.put(player.getWorld().getName(), worldPlayers);
            }
        }

        return playerMap;
    }


    private class TimerThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(5000);
                    StatusModule.this.refresh();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
