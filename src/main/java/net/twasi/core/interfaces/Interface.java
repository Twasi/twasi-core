package net.twasi.core.interfaces;

import net.twasi.core.database.UserStore;
import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;

import java.util.List;

/**
 * Static class that loads twitch interfaces and joins channels
 */
public class Interface {

    static InterfaceLoader loader;

    /**
     * Starts the InterfaceLoader and join all already existing channels.
     */
    public static void load() {
        loader = new InterfaceLoader();

        List<User> users = UserStore.getUsers();

        // Join existing channels (after restart)
        TwasiLogger.log.info("Joining Twitch channels...");
        int success = 0;
        int failed = 0;
        for (User user : users) {
            TwitchInterface bot = loader.createNewTwitch(new Streamer(user));
            if (bot.connect()) {
                success++;
            } else {
                failed++;
            }
        }
        TwasiLogger.log.info(success + " Twitch channels joined. (" + failed + " failed)");
    }
}
