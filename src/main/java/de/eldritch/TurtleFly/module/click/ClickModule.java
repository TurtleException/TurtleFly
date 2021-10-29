package de.eldritch.TurtleFly.module.click;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Objects;

/**
 * Allows custom interactions when players click on each other.
 */
public class ClickModule extends PluginModule {
    public ClickModule() throws PluginModuleEnableException {
        super();

        this.registerListeners();
        this.registerCommands();
    }


    private void registerListeners() {
        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(PlayerInteractEntityEvent event) {
                if (event.getPlayer().getInventory().getItem(event.getHand()).getType().isAir()) {
                    onClick(event.getPlayer(), event.getRightClicked());
                }
            }
        }, TurtleFly.getPlugin());
    }

    private void registerCommands() {
        try {
            Objects.requireNonNull(TurtleFly.getPlugin().getCommand("click")).setExecutor(new ClickCommand(this));
        } catch (NullPointerException e) {
            TurtleFly.getPlugin().getLogger().warning("Unable to register command '/click'.");
        }
    }

    private void onClick(Player source, Entity target) {
        String message = getConfig().getString(source.getUniqueId() + "." + target.getUniqueId() + ".message", null);
        Particle particle = getConfig().getObject(source.getUniqueId() + "." + target.getUniqueId() + ".particle", Particle.class, getConfig().getObject(source.getUniqueId() + ".default.particle", Particle.class, Particle.NOTE));

        if (message != null && target instanceof Player)
            ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§c" + message));
        target.getWorld().spawnParticle(particle, target.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5);
    }
}
