package net.twasi.core.instances;

import net.twasi.core.database.models.EventMessage;
import net.twasi.core.database.models.EventMessageType;
import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;

import java.util.ArrayList;
import java.util.List;

public class InstanceManager {

    private List<TwasiInterface> interfaces = new ArrayList<>();

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
            start(u);
        }
        return true;
    }

    public boolean hasRegisteredInstance(User user) {
        return interfaces.stream().anyMatch(twasiInterface -> twasiInterface.getStreamer().getUser().getId().equals(user.getId()));
    }

    public TwasiInterface getByUser(User user) {
        return (TwasiInterface) interfaces.stream().filter(twasiInterface -> twasiInterface.getStreamer().getUser().getId().equals(user.getId())).toArray()[0];
    }

    public boolean stop(User user) {
        if (!hasRegisteredInstance(user)) {
            TwasiLogger.log.info("Tried to stop interface for user " + user.getTwitchAccount().getUserName() + " but no instance is running.");
            return false;
        }

        TwasiInterface inf = getByUser(user);

        inf.disconnect();
        inf.onDisable();

        interfaces.remove(inf);

        user.addMessage(new EventMessage("Instance stopped.", EventMessageType.INFO));
        return true;
    }

    public boolean start(User user) {
        if (user.getConfig().isActivated()) {
            TwasiInterface inf = new TwitchInterface(new Streamer(user));

            inf.onEnable();
            inf.connect();

            registerInterface(inf);
            user.addMessage(new EventMessage("Instance started.", EventMessageType.INFO));
            return true;
        }
        return false;
    }

    public boolean restart(User user) {
        return stop(user) && start(user);
    }

    public List<TwasiInterface> getInterfaces() {
        return interfaces;
    }
}
