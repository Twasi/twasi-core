package net.twasi.core.database.models;

import net.twasi.core.config.Config;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("users")
public class User {

    private static TwitchAccount defaultAccount = new TwitchAccount(Config.catalog.twitch.defaultName, new AccessToken(Config.catalog.twitch.defaultToken), Config.catalog.twitch.defaultUserId);

    @Id
    private ObjectId id;
    private TwitchAccount twitchAccount;
    private TwitchAccount twitchBotAccount;

    public User() {};

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public TwitchAccount getTwitchAccount() {
        return twitchAccount;
    }

    public void setTwitchAccount(TwitchAccount twitchAccount) {
        this.twitchAccount = twitchAccount;
    }

    public TwitchAccount getTwitchBotAccount() {
        return twitchBotAccount;
    }

    public TwitchAccount getTwitchBotAccountOrDefault() {
        if (twitchBotAccount == null) {
            return defaultAccount;
        } else {
            return twitchBotAccount;
        }
    }

    public void setTwitchBotAccount(TwitchAccount twitchBotAccount) {
        this.twitchBotAccount = twitchBotAccount;
    }
}
