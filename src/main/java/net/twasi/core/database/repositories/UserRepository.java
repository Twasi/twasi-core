package net.twasi.core.database.repositories;

import dev.morphia.query.Query;
import net.twasi.core.database.actions.UserActions;
import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository extends Repository<User> {

    public User getByTwitchId(String twitchId) {
        User user = query().field("twitchAccount.twitchId").equal(twitchId).get();

        /* if (cache.exist(user)) {
            return cache.get(user);
        } else {
            cache.add(user);
            return user;
        } */
        return user;
    }

    public User getByTwitchName(String twitchname) {
        return query().field("twitchAccount.userName").equal(twitchname).first();
    }

    public User getByTwitchAccountOrCreate(TwitchAccount account, ChannelDTO channelData) {
        Query<User> query = query().field("twitchAccount.twitchId").equal(account.getTwitchId());

        User user;
        if (query.count() == 0) {
            user = UserActions.createNewUser(account);
        } else {
            user = query.first();
        }

        // Update access information
        user.getTwitchAccount().setToken(account.getToken());

        // TODO also update other information (username, email, avatar etc.)
        user.setChannelInformation(channelData);

        store.save(user);

        return user;
    }

    public List<ObjectId> getAllRunningIds() {
        Query<User> query = query();
        query.or(
                query.criteria("status").equal(AccountStatus.OK),
                query.criteria("status").equal(AccountStatus.EMAIL_CONFIRMATION)
        );
        return query.asList().stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    public List<User> getAllRunning() {
        Query<User> query = query();
        query.or(
                query.criteria("status").equal(AccountStatus.OK),
                query.criteria("status").equal(AccountStatus.EMAIL_CONFIRMATION)
        );
        return query.asList();
    }

    public long getThemeInstallations(String themeId) {
       return query().field("installedThemes").contains(themeId).count();
    }
}
