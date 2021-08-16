package de.eldritch.TurtleFly.feature;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface Callable {
    /**
     * Invoked by {@link de.eldritch.TurtleFly.listeners.ListenerPlayerInteractEntity#onEvent(PlayerInteractEntityEvent)}
     * when a relevant Event is called. This method checks whether the event is relevant for this specific feature and
     * possibly passes it on.
     *
     * @param event The event provided by the server
     * @throws ClassCastException if the Callable has no use
     *                            for this event.
     */
    void onEvent(Event event) throws ClassCastException;

    /**
     * Invoked by {@link de.eldritch.TurtleFly.commands.CommandTurtleFly#onCommand(CommandSender, Command, String, String[])}
     * when a plugin command is executed.
     *
     * @param sender Source of the command
     * @param args Delivered arguments.
     *
     * @return true if the command was successfull.
     */
    boolean onCommand(CommandSender sender, String[] args);
}
