package net.twasi.core.graphql.model;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.language.SourceLocation;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDTO {
    private User user;

    public UserDTO(User user) {
        this.user = user;
    }

    public String getId() {
        return user.getId().toString();
    }

    public TwitchAccountDTO getTwitchAccount() {
        return new TwitchAccountDTO(user.getTwitchAccount());
    }

    public List<String> getInstalledPlugins() {
        return user.getInstalledPlugins();
    }

    public List<EventMessageDTO> getEvents() {
        return EventMessageDTO.fromEvents(user.getEvents());
    }

    public StreamDTO getLatestStream() {
        return null; // TODO
    }

    public List<StreamDTO> getStreams() {
        return new ArrayList<>(); // TODO
    }

    public CurrentUserStatsDTO getCurrentStats() {
        return null; // TODO
    }

    public AllTimeStatsDTO getAlltimeStats() {
        return null; // TODO
    }

    public UserRank getRank() {
        return UserRank.valueOf(user.getRank());
    }

    public PluginDetailsDTO installPlugin(String name) {
        TwasiPlugin plugin = ServiceRegistry.get(PluginManagerService.class).getByName(name);

        TwasiInterface instance = ServiceRegistry.get(InstanceManagerService.class).getByUser(user);

        if (instance == null) {
            return null;
        }

        instance.installPlugin(plugin);

        return new PluginDetailsDTO(
                plugin.getDescription(),
                ServiceRegistry.get(InstanceManagerService.class)
                        .getByUser(user)
                        .getPlugins()
                        .stream()
                        .anyMatch(p -> p.getCorePlugin().getName().equalsIgnoreCase(name)));
    }

    public PluginDetailsDTO uninstallPlugin(String name) {
        TwasiPlugin plugin = ServiceRegistry.get(PluginManagerService.class).getByName(name);
        ServiceRegistry.get(InstanceManagerService.class).getByUser(user).uninstallPlugin(plugin);

        return new PluginDetailsDTO(
                plugin.getDescription(),
                ServiceRegistry.get(InstanceManagerService.class)
                        .getByUser(user)
                        .getPlugins()
                        .stream()
                        .anyMatch(p -> p.getCorePlugin().getName().equalsIgnoreCase(name)));
    }
}
