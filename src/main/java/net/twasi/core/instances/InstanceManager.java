package net.twasi.core.instances;

import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;

import java.util.ArrayList;
import java.util.List;

public class InstanceManager {

    public List<TwasiInterface> interfaces = new ArrayList<>();

    /**
     * Registers a single interface (e.g. if someone starts the bot aferwards)
     * @param inf The interface to register
     * @return if the interface was registered successfully
     */
    public boolean registerInterface(TwasiInterface inf) {
        if (interfaces.contains(inf)) {
            TwasiLogger.log.info("Tried to register Interface which is already registered (ignored): " + inf.toString());
            return false;
        }

        interfaces.add(inf);
        TwasiLogger.log.debug("Registered interface: " + inf.toString());
        return true;
    }

    /**
     * Starts and register all interfaces for the User array.
     * This should be called after startup to initially start all users.
     * @param users The list of Users
     * @return if all users were started successfully.
     */
    public boolean startForAllUsers(List<User> users) {
        for(User u : users) {
            if (u.getConfig().isActivated()) {
                Streamer streamer = new Streamer(u);
                TwasiInterface inf = new TwitchInterface(streamer);
                registerInterface(inf);
            }
        }
        return true;
    }
}
