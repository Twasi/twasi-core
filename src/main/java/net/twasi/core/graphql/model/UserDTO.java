package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.graphql.TwasiGraphQLHandledException;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.LifecycleManagement;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.PluginManagerService;

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

    public String getBanner() {
        if (user.getChannelInformation() == null) {
            return null;
        }
        return user.getChannelInformation().getProfileBanner();
    }

    public PluginDetailsDTO installPlugin(String name) {
        TwasiPlugin plugin = ServiceRegistry.get(PluginManagerService.class).getByName(name);

        TwasiInterface instance = ServiceRegistry.get(InstanceManagerService.class).getByUser(user);

        if (plugin == null || plugin.getDescription().isHidden()) {
            throw new TwasiGraphQLHandledException("The requested plugin was not found or is not ready for installation.", "plugin.notfound");
        }

        if (instance == null) {
            if (!user.getInstalledPlugins().contains(plugin.getName())) {

                try {
                    TwasiUserPlugin userPlugin = plugin.getUserPluginClass().asSubclass(TwasiUserPlugin.class).newInstance();
                    TwasiInterface inf = new TwitchInterface(new Streamer(user));
                    LifecycleManagement.initiate(userPlugin, inf, plugin);
                    LifecycleManagement.handleInstall(userPlugin);
                } catch (Exception e) {
                    return new PluginDetailsDTO(plugin, user, false);
                }

                user.getInstalledPlugins().add(plugin.getName());
                DataService.get().get(UserRepository.class).commit(user);
            }
            return new PluginDetailsDTO(plugin, user, true);
        }

        instance.installPlugin(plugin);

        return new PluginDetailsDTO(
                plugin,
                user,
                ServiceRegistry.get(InstanceManagerService.class)
                        .getByUser(user)
                        .getPlugins()
                        .stream()
                        .anyMatch(p -> p.getCorePlugin().getName().equalsIgnoreCase(name)));
    }

    public PluginDetailsDTO uninstallPlugin(String name) {
        TwasiPlugin plugin = ServiceRegistry.get(PluginManagerService.class).getByName(name);

        if (plugin.getDescription().isHidden())
            throw new TwasiGraphQLHandledException("The requested plugin may not be uninstalled.", "plugin.uninstallforbidden");

        ServiceRegistry.get(InstanceManagerService.class).getByUser(user).uninstallPlugin(plugin);

        return new PluginDetailsDTO(
                plugin,
                user,
                ServiceRegistry.get(InstanceManagerService.class)
                        .getByUser(user)
                        .getPlugins()
                        .stream()
                        .anyMatch(p -> p.getCorePlugin().getName().equalsIgnoreCase(name)));
    }
}
