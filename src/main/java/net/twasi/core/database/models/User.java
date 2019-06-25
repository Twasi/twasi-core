package net.twasi.core.database.models;

import net.twasi.core.database.models.permissions.PermissionEntity;
import net.twasi.core.database.models.permissions.PermissionEntityType;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.database.models.permissions.Permissions;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.PluginManagerService;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.twitchapi.kraken.channels.response.ChannelDTO;
import org.mongodb.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity("users")
public class User extends BaseEntity {

    private static TwitchAccount defaultAccount;

    private TwitchAccount twitchAccount;
    private TwitchAccount twitchBotAccount;

    private String JWTSecret;

    private GlobalConfig config;

    private List<Permissions> permissions;

    private List<EventMessage> events;

    private List<String> installedPlugins;

    private AccountStatus status;

    private ChannelDTO channelInformation;

    private UserRank rank;

    public User() {
        if (ServiceRegistry.has(PluginManagerService.class)) {
            installedPlugins = PluginManagerService.get().getDefaultPlugins();
        }
        if (ServiceRegistry.has(ConfigService.class)) {
            defaultAccount = new TwitchAccount(
                    ConfigService.get().getCatalog().twitch.defaultName,
                    null,
                    new AccessToken(ConfigService.get().getCatalog().twitch.defaultToken),
                    ConfigService.get().getCatalog().twitch.defaultUserId,
                    new ArrayList<>()
            );
        }
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
            permissions = new ArrayList<>(new ArrayList<>(Arrays.asList(new Permissions(
                    Collections.singletonList(
                            new PermissionEntity(
                                    PermissionEntityType.GROUP,
                                    PermissionGroups.BROADCASTER,
                                    null
                            )
                    ),
                    new ArrayList<>(Arrays.asList("twasi.admin", "twasi.full_panel", "twasi.user")),
                    "twasi_admin"
            ), new Permissions(
                    Collections.singletonList(
                            new PermissionEntity(
                                    PermissionEntityType.GROUP,
                                    PermissionGroups.MODERATOR,
                                    null
                            )
                    ),
                    new ArrayList<>(Arrays.asList("twasi.moderation", "twasi.mod_panel", "twasi.user")),
                    "twasi_mod"
            ), new Permissions(
                    Collections.singletonList(
                            new PermissionEntity(
                                    PermissionEntityType.GROUP,
                                    PermissionGroups.VIEWER,
                                    null
                            )
                    ),
                    new ArrayList<>(Collections.singletonList("twasi.user")),
                    "twasi_user"
            ))));
        }
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    /**
     * Checks if the given TwitchAccount has the permission to do a certain thing on the User's channel
     *
     * @param account       The TwitchAccount to check
     * @param permissionKey The Permission to check
     * @return if the user is allowed to take the action
     */
    public boolean hasPermission(TwitchAccount account, String permissionKey) {

        // Team has global access
        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchId(account.getTwitchId());
        if (user != null && user.getRank().equals(UserRank.TEAM)) {
            return true;
        }

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

    public void setInstalledPlugins(List<String> installedPlugins) {
        this.installedPlugins = installedPlugins;
    }

    public void setEvents(List<EventMessage> events) {
        this.events = events;
    }

    public void addMessage(EventMessage message) {
        // TODO
        /* getEvents().add(message);
        Database.getStore().save(this);*/
    }

    public AccountStatus getStatus() {
        if (status == null) {
            status = AccountStatus.SETUP;
        }
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Permissions getPermissionByName(String name) {
        return getPermissions()
                .stream()
                .filter(perm -> perm.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public UserRank getRank() {
        if (rank == null) {
            rank = UserRank.STREAMER;
        }
        return rank;
    }

    public ChannelDTO getChannelInformation() {
        return channelInformation;
    }

    public void setChannelInformation(ChannelDTO channelInformation) {
        this.channelInformation = channelInformation;
    }
}
