package de.eldritch.TurtleFly.module.status;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("Server Status");
        builder.setDescription("Letztes Update: " + TimeFormat.RELATIVE.now() + ".");

        // online players (per world)
        Map<String, Collection<Player>> players = this.getOnlinePlayers();
        players.forEach((world, players1) -> {
            StringBuilder str = new StringBuilder();
            players1.forEach(player -> str.append(player.getDisplayName()).append("\n"));

            // check for a custom world name in the config
            try {
                if (Objects.requireNonNull(getConfig().getConfigurationSection("worlds")).contains(world)) {
                    builder.addField("Spieler | " + Objects.requireNonNull(getConfig().getConfigurationSection("worlds")).get(world), str.toString(), true);
                }
            } catch (NullPointerException ignored) {
                builder.addField("Spieler | " + world, str.toString(), true);
            }
        });


        this.updateMessage(builder.build());
    }

    /**
     * Updates the Discord status embed message. If the message does not
     * already exist a new one is created.
     * @param embed The new embed version.
     */
    private void updateMessage(MessageEmbed embed) {
        // check wether API is online
        if (TurtleFly.getPlugin().getDiscordAPI() == null)
            return;

        // try to find the existing message
        final Message[] message = {null};
        try {
            // check if a new message has been created in the last refresh
            if (getConfig().getString("discord.message", "null").equals("latest")) {
                Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI().getGuild().getTextChannelById(
                        getConfig().getString("discord.channel", "null")))
                        .getHistory().retrievePast(1).map(messages -> messages.get(0)).queue(message1 -> message[0] = message1);
            }

            message[0] = Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI().getGuild().getTextChannelById(
                    getConfig().getString("discord.channel", "null"))).retrieveMessageById(
                    getConfig().getString("discord.message", "null")).complete();
        } catch (NullPointerException ignored) {}

        // create new message if not exists
        if (message[0] == null) {
            try {
                Objects.requireNonNull(TurtleFly.getPlugin().getDiscordAPI().getGuild().getTextChannelById(
                        getConfig().getString("discord.channel", "null"))).sendMessageEmbeds(embed).queue();
                getConfig().set("discord.message", "latest"); // this is an ugly approach but idk what else to do
            } catch (NullPointerException ignored) {}
            return;
        }

        // update existing message
        message[0].editMessageEmbeds(embed).queue();
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
