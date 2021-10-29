package de.eldritch.TurtleFly.module.click;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClickCommand implements CommandExecutor {
    private final ClickModule MODULE;

    public ClickCommand(ClickModule module) {
        this.MODULE = module;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        if (strings.length < 1) return false;

        if (strings.length < 2)
            return this.get((Player) commandSender, strings[0]);

        // change particle
        boolean b = this.setParticle((Player) commandSender, strings[0], strings[1]);

        // change message
        if (strings.length > 2 && b) {
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < strings.length; i++)
                builder.append(strings[i]).append(i == strings.length - 1 ? "" : " ");

            b = this.setMessage((Player) commandSender, strings[0], builder.toString());
        }

        return b;
    }

    /**
     * Provides the current settings for the specified target.
     * @param sender The {@link Player} calling the command.
     * @param target Should be either <code>default</code> or the
     *               {@link java.util.UUID} of a {@link Player}.
     * @return true if the request was handled successfully.
     */
    private boolean get(Player sender, String target) {
        ComponentBuilder builder = new ComponentBuilder().color(ChatColor.YELLOW);

        if (target.equals("default")) {
            Particle particle = MODULE.getConfig().getObject(sender.getUniqueId() + ".default.particle", Particle.class, null);
            String message = MODULE.getConfig().getString(sender.getUniqueId() + ".default.message", null);

            if (particle == null && message == null) {
                builder.append("Du hast keine Standard-Partikel /-Nachricht eingestellt!")
                        .append(new ComponentBuilder()
                                .color(ChatColor.DARK_GRAY)
                                .append(" >CMD<")
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Command-Template")))
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/click default ")).create());
                sender.spigot().sendMessage(builder.create());
                return true;
            }

            if (particle != null) {
                builder.append("Standard-Partikel: ")
                        .append(ChatColor.ITALIC.toString())
                        .append(particle.name())
                        .color(ChatColor.RESET)
                        .color(ChatColor.YELLOW)
                        .append(".");

                if (message != null) builder.append("\n");
            }

            if (message != null) {
                builder.append("Standard-Nachricht: \"")
                        .append(ChatColor.ITALIC.toString())
                        .append(message)
                        .color(ChatColor.RESET)
                        .color(ChatColor.YELLOW)
                        .append("\".");
            }
        } else {
            Player player = Bukkit.getPlayer(target);
            if (player == null) return false;

            Particle particle = MODULE.getConfig().getObject(sender.getUniqueId() + "." + player.getUniqueId() + ".particle", Particle.class, null);
            String message = MODULE.getConfig().getString(sender.getUniqueId() + "." + player.getUniqueId() + ".message", null);

            if (particle == null && message == null) {
                builder.append("Du hast keine Standard-Partikel /-Nachricht für " + player.getName() + " eingestellt!")
                        .append(new ComponentBuilder()
                                .color(ChatColor.DARK_GRAY)
                                .append(" >CMD<")
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Command-Template")))
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/click " + player.getName() + " ")).create());
                sender.spigot().sendMessage(builder.create());
                return true;
            }

            if (particle != null) {
                builder.append("Standard-Partikel für ")
                        .append(player.getName())
                        .append(": ")
                        .append(ChatColor.ITALIC.toString())
                        .append(particle.name())
                        .color(ChatColor.RESET)
                        .color(ChatColor.YELLOW)
                        .append(".");

                if (message != null) builder.append("\n");
            }

            if (message != null) {
                builder.append("Standard-Nachricht für ")
                        .append(player.getName())
                        .append(": \"")
                        .append(ChatColor.ITALIC.toString())
                        .append(message)
                        .color(ChatColor.RESET)
                        .color(ChatColor.YELLOW)
                        .append("\".");
            }
        }

        sender.spigot().sendMessage(builder.create());
        return true;
    }

    /**
     * Changes the particle for the provided target.
     * @param target Should be either <code>default</code> or the
     *               {@link java.util.UUID} of a {@link Player}.
     * @param particleStr String representation of the new {@link Particle}.
     * @return true if the request was handled successfully.
     */
    private boolean setParticle(Player sender, String target, String particleStr) {
        Particle particle;
        try {
            particle = Particle.valueOf(particleStr);
        } catch (IllegalArgumentException e) {
            return false;
        }

        Player player = Bukkit.getPlayer(target);
        if (player != null) {
            MODULE.getConfig().set(sender.getUniqueId() + "." + player.getUniqueId() + ".particle", particle);
            return true;
        } else if (target.equals("default")) {
            MODULE.getConfig().set(sender.getUniqueId() + ".default.particle", particle);
            return true;
        }
        return false;
    }

    /**
     * Changes the particle for the provided target.
     * @param target Should be either <code>default</code> or the
     *               {@link java.util.UUID} of a {@link Player}.
     * @param message The new message.
     * @return true if the request was handled successfully.
     */
    private boolean setMessage(Player sender, String target, String message) {
        Player player = Bukkit.getPlayer(target);
        if (player != null) {
            MODULE.getConfig().set(sender.getUniqueId() + "." + player.getUniqueId() + ".particle", message);
            return true;
        } else if (target.equals("default")) {
            MODULE.getConfig().set(sender.getUniqueId() + ".default.particle", message);
            return true;
        }
        return false;
    }
}
