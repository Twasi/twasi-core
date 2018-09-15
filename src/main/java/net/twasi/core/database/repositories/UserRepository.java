package net.twasi.core.database.repositories;

import net.twasi.core.database.actions.UserActions;
import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository extends Repository<User> {

    public User getByTwitchId(String twitchId) {
        User user = store.createQuery(User.class).field("twitchAccount.twitchId").equal(twitchId).get();

        if (cache.exist(user)) {
            return cache.get(user);
        } else {
            cache.add(user);
            return user;
        }
    }

    public User getByTwitchAccountOrCreate(TwitchAccount account) {
        Query<User> query = store.createQuery(User.class).field("twitchAccount.twitchId").equal(account.getTwitchId());

        User user;
        if (query.count() == 0) {
            user = UserActions.createNewUser(account);
        } else {
            user = query.get();
        }

        // Update access & refresh token
        user.getTwitchAccount().setToken(account.getToken());
        store.save(user);

        return user;
    }

    public List<ObjectId> getAllRunningIds() {
        Query<User> query = store.createQuery(User.class);
        query.or(
                query.criteria("status").equal(AccountStatus.OK),
                query.criteria("status").equal(AccountStatus.EMAIL_CONFIRMATION)
        );
        List<ObjectId> ids = query.asList().stream().map(e -> e.getId()).collect(Collectors.toList());
        return ids;
    }

    public List<User> getAllRunning() {
        Query<User> query = store.createQuery(User.class);
        query.or(
                query.criteria("status").equal(AccountStatus.OK),
                query.criteria("status").equal(AccountStatus.EMAIL_CONFIRMATION)
        );
        List<User> users = query.asList();
        return users;
    }
}
