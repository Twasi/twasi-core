package net.twasi.core.services.providers;

import net.twasi.core.database.models.EventMessage;
import net.twasi.core.database.models.EventMessageType;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class InstanceManagerService implements IService {
    private List<TwasiInterface> interfaces = new ArrayList<>();

    /**
     * Registers a single interface (e.g. if someone starts the bot aferwards)
     *
     * @param inf The interface to register
     * @return if the interface was registered successfully
     */
    public boolean registerInterface(TwasiInterface inf) {
        if (interfaces.contains(inf)) {
            TwasiLogger.log.info("Tried to register Interface which is already registered (ignored): " + inf.toString());
            return false;
        }
        if (interfaces.stream().anyMatch(twitchInf -> twitchInf.getStreamer().getUser().getTwitchAccount().getTwitchId().equals(inf.getStreamer().getUser().getTwitchAccount().getTwitchId()))) {
            TwasiLogger.log.info("Tried to register Interface for a streamer, that already has one: " + inf.getStreamer().getUser().getTwitchAccount().getDisplayName());
            return false;
        }

        interfaces.add(inf);
        TwasiLogger.log.debug("Registered interface for streamer " + inf.getStreamer().getUser().getTwitchAccount().getUserName());
        return true;
    }

    /**
     * Starts and register all interfaces for the User array.
     * This should be called after startup to initially start all users.
     */
    public void startForAllUsers() {
        List<User> shouldRunIn = ServiceRegistry.get(DataService.class).get(UserRepository.class).getAllRunning();
        TwasiLogger.log.info("Initial starting instances. Number of instances: " + shouldRunIn.size());
        for (User user : shouldRunIn) {
            start(user);
        }
    }

    public boolean hasRegisteredInstance(User user) {
        return interfaces.stream().anyMatch(twasiInterface -> twasiInterface.getStreamer().getUser().getId().equals(user.getId()));
    }

    public TwasiInterface getByUser(User user) {
        return interfaces.stream().filter(twasiInterface -> twasiInterface.getStreamer().getUser().getId().equals(user.getId())).findFirst().orElse(null);
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

            inf.enableInstalledPlugins();

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
