package de.eldritch.TurtleFly.util;

import de.eldritch.TurtleFly.TurtleFly;

public abstract class Runnable implements java.lang.Runnable {
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public void cancel() {
        TurtleFly.getPlugin().getServer().getScheduler().cancelTask(id);
    }
}
