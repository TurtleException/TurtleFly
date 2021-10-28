package de.eldritch.TurtleFly.module;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.chat.ChatModule;
import de.eldritch.TurtleFly.module.sleep.SleepModule;
import de.eldritch.TurtleFly.module.status.StatusModule;
import de.eldritch.TurtleFly.module.sync.SyncModule;
import de.eldritch.TurtleFly.module.click.ClickModule;
import de.eldritch.TurtleFly.module.helmet.HelmetModule;
import de.eldritch.TurtleFly.module.pets.PetsModule;

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

        /*  !!! KEEP THIS PART UPDATED !!!  */
        modClasses.put(ChatModule.class, new Object[]{});
        modClasses.put(ClickModule.class, new Object[]{});
        // modClasses.put(CompassModule.class, new Object[]{});
        modClasses.put(HelmetModule.class, new Object[]{});
        modClasses.put(PetsModule.class, new Object[]{});
        modClasses.put(SleepModule.class, new Object[]{});
        modClasses.put(StatusModule.class, new Object[]{});
        modClasses.put(SyncModule.class, new Object[]{});
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
                if (!modules.add(obj)) // stop if another object of this module has already been registered
                    throw new PluginModuleEnableException(clazz.getSimpleName() + " already exists.");
                TurtleFly.getPlugin().getLogger().info("Registered " + clazz.getSimpleName() + ".");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | PluginModuleEnableException e) {
                TurtleFly.getPlugin().getLogger().log(Level.WARNING, "Unable to instantiate " + clazz.getSimpleName() + ". It will be ignored.", e);
            }
        });
    }

    /**
     * @return Set of all currently registered modules.
     */
    public HashSet<PluginModule> getRegisteredModules() {
        return this.modules;
    }
}
