package net.twasi.core.database.store;

import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.services.mail.MailService;
import net.twasi.core.services.mail.MailTemplates;

import java.util.List;
import java.util.UUID;

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
     * If the user doesn't exist he will be registered.
     */
    public static User getOrCreate(TwitchAccount account) {
        // List<User> users = Database.getStore().createQuery(User.class).filter("twitchAccount.twitchId =", account.getTwitchId()).asList();
        User user = getById(account.getTwitchId());
        if (user == null) {
            // Register new user
            user = new User();
            user.setTwitchAccount(account);

            if (Config.getCatalog().mail.enabled) {
                String confirmationCode = user.getTwitchAccount().getUserName() + "_" + UUID.randomUUID().toString();
                user.getTwitchAccount().setConfirmationCode(confirmationCode);

                // Send welcome mail
                MailService.getService().getMailer().sendMail(MailTemplates.getEmailConfirmationMail(user.getTwitchAccount().getEmail(), user.getTwitchAccount().getUserName(), confirmationCode));
            } else {
                user.setStatus(AccountStatus.OK);
            }

            users.add(user);
            Database.getStore().save(user);

            return user;
        }
        return user;
    }

    public static User getById(String twitchid) {
        /* List<User> users = Database.getStore().createQuery(User.class).filter("twitchAccount.twitchId =", twitchid).asList();
        if (users.size() == 0) {
            return null;
        }
        return users.get(0); */
        return users.stream().filter(user -> user.getTwitchAccount().getTwitchId().equalsIgnoreCase(twitchid)).findFirst().orElse(null);
    }

    public static void updateUser(User user) {
        InstanceManagerService.getService().stop(user);
        users.remove(user);

        User newUser = getById(user.getTwitchAccount().getTwitchId());
        users.add(newUser);

        InstanceManagerService.getService().start(user);
    }

}
