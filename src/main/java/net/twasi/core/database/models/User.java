package net.twasi.core.database.models;

import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.permissions.PermissionEntity;
import net.twasi.core.database.models.permissions.PermissionEntityType;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.database.models.permissions.Permissions;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity("users")
public class User {

    private static TwitchAccount defaultAccount;

    @Id
    private ObjectId id;

    private TwitchAccount twitchAccount;
    private TwitchAccount twitchBotAccount;

    private String JWTSecret;

    private GlobalConfig config;

    private List<Permissions> permissions;

    private List<EventMessage> events;

    private List<String> installedPlugins;

    private AccountStatus status;

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
            GlobalConfig config = GlobalConfig.getDefault();
            setConfig(config);
            return config;
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
            permissions = new ArrayList<>(Collections.singletonList(new Permissions(
                    Collections.singletonList(
                            new PermissionEntity(
                                    PermissionEntityType.GROUP,
                                    PermissionGroups.BROADCASTER,
                                    null
                            )
                    ),
                    Arrays.asList("twasi.admin", "twasi.full_panel", "twasi.user"),
                    "twasi_admin"
            )));
        }
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(TwitchAccount account, String permissionKey) {
        for (Permissions perm : getPermissions()) {
            if (perm.hasPermission(account, permissionKey)) {
                return true;
            }
        }
        return false;
    }

    public List<EventMessage> getEvents() {
        if (events == null) {
            events = new ArrayList<>();
        }
        return events;
    }

    public List<String> getInstalledPlugins() {
        if (installedPlugins == null) {
            installedPlugins = new ArrayList<>();
        }
        return installedPlugins;
    }

    public void setEvents(List<EventMessage> events) {
        this.events = events;
    }

    public void addMessage(EventMessage message) {
        getEvents().add(message);
        Database.getStore().save(this);
    }

    public AccountStatus getStatus() {
        if (status == null) {
            status = AccountStatus.EMAIL_CONFIRMATION;
        }
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
