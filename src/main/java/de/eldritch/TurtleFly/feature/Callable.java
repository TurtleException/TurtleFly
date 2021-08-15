package de.eldritch.TurtleFly.feature;

import org.bukkit.event.Event;

public interface Callable {
    /**
     * Invoked by listeners.ListenerPlayerInteract.onEvent()
     * when a PlayerInteractEtityEvent is called. This method
     * checks whether the event is relevant for this specific
     * feature and possibly passes it on.
     * @param event The event provided by the server
     */
    void onEvent(Event event);
}
