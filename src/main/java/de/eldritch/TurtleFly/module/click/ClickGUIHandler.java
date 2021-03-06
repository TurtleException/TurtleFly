package de.eldritch.TurtleFly.module.click;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import de.eldritch.TurtleFly.TurtleFly;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.block.Skull;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ClickGUIHandler {
    private static ClickGUIHandler singleton;

    private final ClickModule MODULE;


    private final TreeMap<String, Particle> particleMap = new TreeMap<>(Map.ofEntries(
            Map.entry("Herzen", Particle.HEART),
            Map.entry("Musik", Particle.NOTE),
            Map.entry("Wütender Villager", Particle.VILLAGER_ANGRY),
            Map.entry("Hexe", Particle.SPELL_WITCH),
            Map.entry("Glühen", Particle.GLOW),
            Map.entry("Portal", Particle.PORTAL),
            Map.entry("Wachs", Particle.WAX_ON)
    ));


    public ClickGUIHandler(ClickModule module) {
        singleton = this;

        this.MODULE = module;
    }

    public static void handle(@NotNull Player source) {
        try {
            Objects.requireNonNull(singleton).generateTargetGui(source);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * Generates the TARGET GUI for a specific {@link HumanEntity}.
     * <p>
     *     A TARGET GUI consists of all players that are currently
     *     whitelisted on the server and a default setting.
     * </p>
     * @param player The player presented with the GUI
     */
    private void generateTargetGui(@NotNull Player player) {
        ChestGui gui = new ChestGui((TurtleFly.getPlugin().getServer().getWhitelistedPlayers().size() / 9) + 1, "Ziel");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane pane = new OutlinePane(0, 0, 9, gui.getRows());

        // default
        pane.addItem(new GuiItem(new ItemStack(this.getDefaultItem()), event -> generateParticleGui(player, null)));

        // players
        this.getWhitelistSkulls(player).forEach(
                skull -> pane.addItem(new GuiItem(skull, inventoryClickEvent -> this.generateParticleGui(player, ((SkullMeta) skull.getItemMeta()).getOwningPlayer())))
        );

        gui.addPane(pane);
        gui.show(player);
        gui.update();
    }

    /**
     * @implNote Temporarily deactivated.
     */
    private void generateMessageGui(@NotNull Player player, @Nullable OfflinePlayer target) {
        AnvilGui gui = new AnvilGui(target == null ? "Standard-Nachricht" : "Nachricht für " + target.getName());
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        MODULE.reloadConfig();
        Particle particle = MODULE.getConfig().getObject(player.getUniqueId() + "." + (target == null ? "default" : target.getUniqueId()) + ".particle", Particle.class, ClickModule.getDefaultParticle());

        gui.getFirstItemComponent().setItem(new GuiItem(target == null ? getDefaultItem() : getSkull(target),
                inventoryClickEvent -> this.generateTargetGui(player)), 0, 0);
        gui.getSecondItemComponent().setItem(new GuiItem(new ItemStack(Material.NETHER_STAR, 1),
                inventoryClickEvent -> this.generateParticleGui(player, target)), 0, 0);
        gui.getResultComponent().setItem(new GuiItem(new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1),
                inventoryClickEvent -> this.saveMessage(player, target, ((AnvilGui) inventoryClickEvent.getClickedInventory()).getRenameText())), 0, 0);

        gui.show(player);
        gui.update();
    }

    private void generateParticleGui(@NotNull Player player, @Nullable OfflinePlayer target) {
        String targetID = target == null ? "default" : target.getUniqueId().toString();

        String current = MODULE.getConfig().getString(player.getUniqueId() + "." + targetID + ".particle", "null");

        ChestGui gui = new ChestGui(((particleMap.size() + 1) / 9) + 1, target == null ? "Standard-Partikel" : "Partikel für " + target.getName());
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // main particles
        OutlinePane pane = new OutlinePane(0, 0, 9, gui.getRows());

        particleMap.forEach((str, particle) -> {
            ItemStack item = new ItemStack(particle.name().equals(current) ? Material.ENDER_EYE : Material.ENDER_PEARL, 1);
            ItemMeta  meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(ChatColor.YELLOW + str);
                meta.setLore(Collections.singletonList(ChatColor.ITALIC.toString() + ChatColor.DARK_GRAY.toString() + particle.name()));
            }

            item.setItemMeta(meta);
            pane.addItem(new GuiItem(item, inventoryClickEvent -> {
                MODULE.reloadConfig();
                MODULE.getConfig().set(player.getUniqueId() + "." + targetID + ".particle", particle.name());
                MODULE.saveConfig();

                player.closeInventory();

                // send message
                ComponentBuilder builder = new ComponentBuilder(TurtleFly.getChatPrefix());
                player.spigot().sendMessage(
                        builder.append(target == null ? "Neue Standard-Einstellungen gespeichert." : ("Neue Einstellungen für " + target.getName() + " gespeichert."))
                                .color(net.md_5.bungee.api.ChatColor.GRAY).create()
                );
            }));
        });

        // extended particle selection
        StaticPane footerPane = new StaticPane(8, gui.getRows() - 1, 1, 1);
        ItemStack extendedSelector = new ItemStack(Material.CHEST);
        ItemMeta  meta = extendedSelector.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Erweiterte Auswahl");
            meta.setLore(Arrays.asList(ChatColor.RED + "Diese Funktion ist ", ChatColor.RED + "noch nicht verfügbar."));
        }
        extendedSelector.setItemMeta(meta);
        footerPane.addItem(new GuiItem(new ItemStack(Material.CHEST),
                inventoryClickEvent -> this.generateExtendedParticleGui(player, target)), 0, 0);

        gui.addPane(pane);
        gui.addPane(footerPane);
        gui.show(player);
        gui.update();
    }

    private void generateExtendedParticleGui(@NotNull Player player, @Nullable OfflinePlayer target) {
        String targetID = target == null ? "default" : target.getUniqueId().toString();

        Particle current = MODULE.getConfig().getObject(player.getUniqueId() + "." + targetID + ".particle", Particle.class);

        ChestGui gui = new ChestGui(6 , target == null ? "Standard-Partikel" : "Partikel für " + target.getName());

        // TODO
    }


    private void saveMessage(Player player, @Nullable OfflinePlayer target, @Nullable String msg) {
        String targetID = target == null ? "default" : target.getUniqueId().toString();

        MODULE.reloadConfig();
        MODULE.getConfig().set(player.getUniqueId() + "." + targetID + ".message", msg);
        MODULE.saveConfig();

        ComponentBuilder builder = new ComponentBuilder(TurtleFly.getChatPrefix());
        player.spigot().sendMessage(
                builder.append(target == null ? "Neue Standard-Einstellungen gespeichert." : ("Neue Einstellungen für " + target.getName() + " gespeichert."))
                        .color(net.md_5.bungee.api.ChatColor.GRAY).create()
        );
    }


    private ItemStack getDefaultItem() {
        ItemStack item = new ItemStack(Material.DIRT, 1);
        ItemMeta  meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Default");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Standard-Einstellung für alle Spieler,",
                    ChatColor.GRAY + "denen nichts eigenes zugewiesen wird."));
            item.setItemMeta(meta);
        }

        return item;
    }

    private TreeSet<ItemStack> getWhitelistSkulls(OfflinePlayer self) {
        Set<OfflinePlayer> whitelist = TurtleFly.getPlugin().getServer().getWhitelistedPlayers();
        whitelist.remove(self);

        TreeSet<ItemStack> skulls = new TreeSet<>(Comparator.comparing(skull -> ((SkullMeta) skull.getItemMeta()).getOwningPlayer().getName()));
        whitelist.stream().map(this::getSkull).forEach(skulls::add);

        return skulls;
    }

    /**
     * Provides the {@link Skull} of an {@link OfflinePlayer}.
     */
    private ItemStack getSkull(OfflinePlayer skullOwner) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if (meta != null) {
            meta.setOwningPlayer(skullOwner);
            meta.setDisplayName(ChatColor.GOLD + (skullOwner.getName() != null ? skullOwner.getName() : "Unknown Player"));
            item.setItemMeta(meta);
        }

        return item;
    }
}
