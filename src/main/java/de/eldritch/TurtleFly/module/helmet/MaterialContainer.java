package de.eldritch.TurtleFly.module.helmet;

import org.bukkit.Material;

import java.util.HashSet;

/**
 * TODO: update
 * Provides a list with all accepted materials for an Event
 * relevant for InteractionFlower, including a check method.
 */
public class MaterialContainer {
    // list for all accepted materials
    private final HashSet<Material> materials = new HashSet<>();

    public MaterialContainer() {
        this.fillMaterials();
    }

    private void fillMaterials() {
        // FLOWERS
        this.materials.add(Material.DANDELION);
        this.materials.add(Material.POPPY);
        this.materials.add(Material.BLUE_ORCHID);
        this.materials.add(Material.ALLIUM);
        this.materials.add(Material.AZURE_BLUET);
        this.materials.add(Material.RED_TULIP);
        this.materials.add(Material.ORANGE_TULIP);
        this.materials.add(Material.WHITE_TULIP);
        this.materials.add(Material.PINK_TULIP);
        this.materials.add(Material.OXEYE_DAISY);
        this.materials.add(Material.BROWN_MUSHROOM);
        this.materials.add(Material.RED_MUSHROOM);
        this.materials.add(Material.SUNFLOWER);
        this.materials.add(Material.LILAC);
        this.materials.add(Material.ROSE_BUSH);
        this.materials.add(Material.PEONY);
        this.materials.add(Material.TALL_GRASS);
        this.materials.add(Material.LARGE_FERN);
        this.materials.add(Material.CORNFLOWER);
        this.materials.add(Material.LILY_OF_THE_VALLEY);
        this.materials.add(Material.WITHER_ROSE);
        this.materials.add(Material.CRIMSON_FUNGUS);
        this.materials.add(Material.WARPED_FUNGUS);
        this.materials.add(Material.CRIMSON_ROOTS);
        this.materials.add(Material.WARPED_ROOTS);
        this.materials.add(Material.NETHER_SPROUTS);
    }


    public boolean hasMaterial(Material material) {
        return this.materials.contains(material);
    }
}