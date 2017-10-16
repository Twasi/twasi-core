package net.twasi.core.database;

import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;

import java.util.List;

/**
 * This class manages users and performs database operations (update, read, create, delete)
 */
public class UserStore {

    private static List<User> users;

    /**
     * Loads all users from the database
     */
    static void loadUsers() {
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

}
