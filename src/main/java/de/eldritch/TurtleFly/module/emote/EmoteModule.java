package de.eldritch.TurtleFly.module.emote;

import de.eldritch.TurtleFly.TurtleFly;
import de.eldritch.TurtleFly.module.PluginModule;
import de.eldritch.TurtleFly.module.PluginModuleEnableException;
import de.eldritch.TurtleFly.module.status.StatusModule;
import de.eldritch.TurtleFly.module.sync.SyncModule;
import net.dv8tion.jda.api.entities.Emote;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Provides Minecraft skin heads as Discord {@link Emote Emotes}
 * to be used by {@link StatusModule} and {@link SyncModule}.
 */
public class EmoteModule extends PluginModule {
    public EmoteModule() throws PluginModuleEnableException {
        super();

        if (TurtleFly.getPlugin().getDiscordAPI() == null)
            throw new PluginModuleEnableException("Module is dependant on JDA connection.");

        this.registerListeners();
    }

    private void registerListeners() {
        TurtleFly.getPlugin().getServer().getPluginManager().registerEvents(new PlayerJoinListener(), TurtleFly.getPlugin());
    }

    /**
     * Retrieves the avatar (face of the skin) of a {@link Player}
     * via <code>https://minotar.net</code>.
     * @param name The name of the {@link Player}.
     * @return Avatar as a byte array.
     * @throws IOException if something goes wrong while requesting
     *                     the avatar.
     */
    public static byte[] retrieveAvatar(String name) throws IOException {
        URL url = new URL("https://minotar.net/helm/" + name + "/256");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (InputStream in = url.openStream()) {
            int n = 0;
            byte[] buffer = new byte[1024];
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
        }

        return out.toByteArray();
    }
}
