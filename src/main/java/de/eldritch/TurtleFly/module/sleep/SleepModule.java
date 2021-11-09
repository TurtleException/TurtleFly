package de.eldritch.TurtleFly.module.sleep;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import de.eldritch.TurtleFly.util.Runnable;
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
     * Recalculates the current amount of {@link Player}s needed to pass a night.
     * <p>
     *     If enough players are sleeping the night will be skipped.
     * </p>
     */
    public void refresh() {
        if ((sleepingPlayers.size() >= (world.getPlayers().size() + 1) / 2) && (world.getTime() > 12000)) {
            final int[] i = {0};
            final long[] tickValues = this.getTickValues(world.getTime(), 24000L, this.getTickAmount(world.getTime()));

            sleepingPlayers.forEach(player -> player.wakeup(true));

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if ((world.getTime() > 12000)) {
                        if (tickValues.length > i[0]) {
                            world.setFullTime(tickValues[i[0]]);
                            i[0]++;
                        } else {
                            cancel();
                        }
                    } else {
                        cancel();
                    }
                }
            };

            int taskId = TurtleFly.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(TurtleFly.getPlugin(), runnable, 0L, 1L);
            runnable.setId(taskId);

            // maximum time a runnable should be able to run
            TurtleFly.getPlugin().getServer().getScheduler().runTaskLaterAsynchronously(TurtleFly.getPlugin(),
                    () -> TurtleFly.getPlugin().getServer().getScheduler().cancelTask(taskId), 600L);

            TurtleFly.getPlugin().getLogger().info("Skipped a night (at least 1/2 of all players were sleeping).");
        }
    }

    /**
     * Provides an array of tick values for a smooth transistion
     * between two specified tick values.
     * @param from The time from wich the array should start.
     * @param to The time at wich the array should stop
     * @param ticks The amount of ticks for the transition.
     * @return An array of length ticks + 1 with time values between from and to.
     */
    private long[] getTickValues(long from, long to, long ticks) {
        long diff = to - from;
        long[] arr = new long[(int) ticks + 1];
        for (int i = 0; i <= ticks; i++)
            arr[i] = (long) (((Math.sin(3D * ((double) i / (double) ticks) - 0.5D * Math.PI) + 1D) / 2D) * diff + from);
        return arr;
    }

    private long getTickAmount(long worldTime) {
        if (worldTime < 16000)
            return getConfig().getLong("ticks.evening", 180L);
        if (worldTime < 20000)
            return getConfig().getLong("ticks.midnight", 120L);
        return getConfig().getLong("ticks.monring", 60L);
    }
}
