package net.twasi.core.database.models;

import net.twasi.core.config.Config;
import net.twasi.core.database.models.permissions.Permissions;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity("users")
public class User {

    private static TwitchAccount defaultAccount;

    @Id
    private ObjectId id;
    private TwitchAccount twitchAccount;
    private TwitchAccount twitchBotAccount;

    private String JWTSecret;

    private Language language;

    private GlobalConfig config;
    private List<Permissions> permissions;

    public User() {
        if (Config.getCatalog() != null) {
            defaultAccount = new TwitchAccount(
                    Config.getCatalog().twitch.defaultName, new AccessToken(Config.getCatalog().twitch.defaultToken),
                    Config.getCatalog().twitch.defaultUserId,
                    new ArrayList<>()
            );
        }
    };

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

    public String getJWTSecret() {
        return JWTSecret;
    }

    public void setJWTSecret(String JWTSecret) {
        this.JWTSecret = JWTSecret;
    }

    public GlobalConfig getConfig() {
        if (config == null) {
            return GlobalConfig.getDefault();
        }
        return config;
    }

    public void setConfig(GlobalConfig config) {
        this.config = config;
    }

    public static TwitchAccount getDefaultAccount() {
        return defaultAccount;
    }

    public static void setDefaultAccount(TwitchAccount defaultAccount) {
        User.defaultAccount = defaultAccount;
    }

    public List<Permissions> getPermissions() {
        if (permissions == null) {
            return new ArrayList<>();
        }
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public boolean hasPermission(TwitchAccount account, String permissionKey) {
        for (Permissions perm : getPermissions()) {
            if (perm.hasPermission(account, permissionKey)) {
                return true;
            }
        }
        return false;
    }

    public boolean doAllPermissionKeysExist(List<String> keys) {
        if (getPermissions() == null || getPermissions().size() == 0) {
            return false;
        }
        for (Permissions perm : getPermissions()) {
            if (!perm.doAllKeysExist(keys)) {
                return false;
            }
        }
        return true;
    }
}
