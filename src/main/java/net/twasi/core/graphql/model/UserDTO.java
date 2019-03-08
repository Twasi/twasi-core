package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.ArrayList;
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

    public UserRank getRank() {
        return user.getRank();
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
