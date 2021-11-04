package de.eldritch.TurtleFly.module.sleep;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SleepEventsListener implements Listener {
    private final SleepModule MODULE;

    public SleepEventsListener(SleepModule module) {
        this.MODULE = module;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled()) return;

        if (event.getPlayer().getWorld().equals(MODULE.world))
            MODULE.sleepingPlayers.add(event.getPlayer());

        MODULE.refresh();
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        MODULE.sleepingPlayers.remove(event.getPlayer());
        MODULE.refresh();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setSleepingIgnored(true);
        MODULE.refresh();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MODULE.sleepingPlayers.remove(event.getPlayer());
        MODULE.refresh();
    }
}
