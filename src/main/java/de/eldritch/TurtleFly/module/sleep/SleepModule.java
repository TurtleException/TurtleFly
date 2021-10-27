package de.eldritch.TurtleFly.module.sleep;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class SleepModule extends PluginModule {
    public HashSet<Player> sleepingPlayers = new HashSet<>();
    public World world;

    public SleepModule() throws PluginModuleEnableException {
        super();

        /*
        It is not officially documented but Bukkit#getWorlds()#get(0)
        should always return the default world of the server.
         */
        world = TurtleFly.getPlugin().getServer().getWorlds().get(0);

        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new SleepEventsListener(this), TurtleFly.getPlugin());
    }

    /**
     * Recalculates the current amount of players needed to pass a night.
     */
    public void refresh() {
        if ((sleepingPlayers.size() >= (world.getPlayers().size() + 1) / 2) && (world.getTime() < 12000)) {
            world.setFullTime(1000);
            sleepingPlayers.clear();
        }
    }
}
