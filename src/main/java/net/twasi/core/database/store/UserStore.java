package net.twasi.core.database.store;

import net.twasi.core.database.Database;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.instances.InstanceManager;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.InstanceManagerService;

import java.util.List;

/**
 * This class manages users and performs database operations (update, read, create, delete)
 */
public class UserStore {

    private static List<User> users;

    /**
     * Loads all users from the database
     */
    public static void loadUsers() {
        TwasiLogger.log.debug("Loading users from database");
        users = Database.getStore().createQuery(User.class).asList();
    }

    /**
     * Writes all users to the database
     */
    static void saveUsers() {
        Database.getStore().save(users);
    }

    /**
     * Get a list of all users
     * @return a list of all users
     */
    public static List<User> getUsers() {
        return users;
    }

    /**
     * Returns a user by TwitchID
     */
    public static User getOrCreate(TwitchAccount account) {
        List<User> users = Database.getStore().createQuery(User.class).filter("twitchAccount.twitchId =", account.getTwitchId()).asList();
        if (users.size() == 0) {
            // User not registered.
            User user = new User();
            user.setTwitchAccount(account);
            Database.getStore().save(user);
            return user;
        }
        return users.get(0);
    }

    public static User getById(String twitchid) {
        List<User> users = Database.getStore().createQuery(User.class).filter("twitchAccount.twitchId =", twitchid).asList();
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    public static void updateUser(User user) {
        InstanceManagerService.getService().stop(user);
        users.remove(user);

        User newUser = getById(user.getTwitchAccount().getTwitchId());
        users.add(newUser);

        InstanceManagerService.getService().start(user);
    }

}
