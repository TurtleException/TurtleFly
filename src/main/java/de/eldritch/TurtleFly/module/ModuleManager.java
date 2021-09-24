package de.eldritch.TurtleFly.module;

import de.eldritch.TurtleFly.Plugin;
import de.eldritch.TurtleFly.module.click.ClickModule;
import de.eldritch.TurtleFly.module.compass.CompassModule;
import de.eldritch.TurtleFly.module.helmet.HelmetModule;
import de.eldritch.TurtleFly.module.pets.PetsModule;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public class ModuleManager {
    private final HashSet<PluginModule> modules = new HashSet<>();


    public ModuleManager() {
        this.registerModules();
    }


    /**
     * Registers modules by instantiating them one at a time.
     * <p><b>IMPORTANT</b>: Update this when adding new modules!</p>
     */
    public void registerModules() {
        HashMap<Class<? extends PluginModule>, Object[]> modClasses = new HashMap<>();

        /*  !!! UPDATE THIS PART !!!  */
        modClasses.put(ClickModule.class, new Object[]{});
        modClasses.put(CompassModule.class, new Object[]{});
        modClasses.put(HelmetModule.class, new Object[]{});
        modClasses.put(PetsModule.class, new Object[]{});
        /* !!! - - - !!!  */

        modClasses.forEach((clazz, params) -> {
            Class<?>[] paramTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }

            PluginModule obj;
            try {
                // instantiate
                obj = clazz.getConstructor(paramTypes).newInstance(params);

                // add to set
                if (modules.add(obj))
                    throw new PluginModuleEnableException(clazz.getSimpleName() + " already exists.");
                Plugin.getPlugin().getLogger().info("Registered " + clazz.getSimpleName() + ".");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | PluginModuleEnableException e) {
                Plugin.getPlugin().getLogger().log(Level.WARNING, "Unable to instantiate " + clazz.getSimpleName() + ". It will be ignored.", e);
            }
        });
    }
}
