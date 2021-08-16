package de.eldritch.TurtleFly.feature;

import de.eldritch.TurtleFly.feature.boop.InteractionBoop;
import de.eldritch.TurtleFly.feature.compass.InteractionCompass;
import de.eldritch.TurtleFly.feature.flowers.InteractionFlower;
import de.eldritch.TurtleFly.feature.wolfi.InteractionWolf;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

public class CallManager {
    private final Map<String, Callable> callables = new HashMap<>();

    public CallManager() {
        this.fillCallables();
    }

    private void fillCallables() {
        callables.clear();

        callables.put("boop", new InteractionBoop());
        callables.put("compass", new InteractionCompass());
        callables.put("flower", new InteractionFlower());
        callables.put("wolfi", new InteractionWolf());
    }

    public void process(Event event) {
        callables.forEach((key, callable) -> {
            try {
                callable.onEvent(event);
            } catch (ClassCastException ignored) {}
        });
    }

    public boolean process(CommandSender sender, String[] args) {
        Callable callable = callables.get(args[1]);

        if (callable != null) {
            return callable.onCommand(sender, args);
        } else {
            // not such feature
            return false;
        }
    }
}
