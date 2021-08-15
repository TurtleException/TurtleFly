package de.eldritch.TurtleFly.feature;

import de.eldritch.TurtleFly.feature.boop.InteractionBoop;
import de.eldritch.TurtleFly.feature.flowers.InteractionFlower;
import de.eldritch.TurtleFly.feature.wolfi.InteractionWolf;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class CallManager {
    private final List<Callable> callables = new ArrayList<>();

    public CallManager() {
        this.fillCallables();
    }

    private void fillCallables() {
        callables.clear();

        callables.add(new InteractionBoop());
        callables.add(new InteractionFlower());
        callables.add(new InteractionWolf());
    }

    public void process(Event event) {
        for (Callable callable : callables) {
            try {
                callable.onEvent(event);
            } catch (ClassCastException ignored) {}
        }
    }
}
