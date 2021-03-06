package net.twasi.core.interfaces.api;

import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.LifecycleManagement;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommand;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.PluginManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TwasiInterface implements TwasiInterfaceInterface {

    private List<TwasiUserPlugin> userPlugins = new ArrayList<>();
    private User user;

    protected TwasiInterface(User user) {
        this.user = user;
    }

    public void enableInstalledPlugins() {
        PluginManagerService pluginManagerService = ServiceRegistry.get(PluginManagerService.class);
        List<String> installedPlugins = new ArrayList<>(user.getInstalledPlugins());

        pluginManagerService.getPlugins().stream().filter(
                p -> p.getDescription().isAutoInstall() && p.getDescription().isHidden()
        ).map(
                p -> p.getDescription().getName()
        ).forEach(installedPlugins::add);

        // Enable all currently installed and auto installing plugins
        installedPlugins.forEach(name -> {
            TwasiPlugin plugin = pluginManagerService.getByName(name);

            if (plugin == null) {
                TwasiLogger.log.warn("Tried to enable plugin '" + name + "' but was not found in plugins folder.");
                return;
            }

            enableUserPlugin(plugin);
        });
    }

    /**
     * Installs a certain user plugin for the given user
     *
     * @param plugin The plugin to install
     * @return whether it was installed successfully
     */
    public boolean installPlugin(TwasiPlugin plugin) {
        if (userPlugins.stream().anyMatch(userPlugin -> userPlugin.getClass().equals(plugin.getUserPluginClass()))) {
            TwasiLogger.log.info("Tried to install userplugin " + plugin.getUserPluginClass() + " twice for Streamer " + getStreamer().getUser().getTwitchAccount().getDisplayName());
            return false;
        }

        // Add to db
        UserRepository userRepo = ServiceRegistry.get(DataService.class).get(UserRepository.class);
        User u = userRepo.getById(user.getId());

        if (u.getInstalledPlugins().contains(plugin.getName())) {
            TwasiLogger.log.info("Tried to install userplugin " + plugin.getUserPluginClass() + " twice (database) for Streamer " + getStreamer().getUser().getTwitchAccount().getDisplayName());
            return false;
        }

        u.getInstalledPlugins().add(plugin.getName());
        userRepo.commit(u);

        try {
            TwasiUserPlugin userPlugin = plugin.getUserPluginClass().asSubclass(TwasiUserPlugin.class).newInstance();

            LifecycleManagement.initiate(userPlugin, this, plugin);
            LifecycleManagement.handleInstall(userPlugin);
            userPlugins.add(userPlugin);
        } catch (InstantiationException e) {
            TwasiLogger.log.error("Cannot instantiate new UserPlugin: no public constructor: " + plugin.getUserPluginClass().getName());
        } catch (IllegalAccessException e) {
            TwasiLogger.log.error("Abnormal UserPlugin: " + e.getMessage());
        }
        return true;
    }

    public void enableUserPlugin(TwasiPlugin plugin) {
        try {
            if (plugin == null) {
                TwasiLogger.log.warn("Tried to enable userplugin but Plugin.jar was not found locally.");
                return;
            }

            if (userPlugins.stream().anyMatch(userPlugin -> userPlugin.getClass().equals(plugin.getUserPluginClass()))) {
                TwasiLogger.log.info("Tried to enable userplugin " + plugin.getUserPluginClass() + " twice for Streamer " + getStreamer().getUser().getTwitchAccount().getDisplayName());
                return;
            }

            TwasiUserPlugin userPlugin = plugin.getUserPluginClass().asSubclass(TwasiUserPlugin.class).newInstance();

            LifecycleManagement.initiate(userPlugin, this, plugin);
            LifecycleManagement.handleEnable(userPlugin);
            userPlugins.add(userPlugin);
        } catch (InstantiationException e) {
            TwasiLogger.log.error("Cannot instantiate new UserPlugin: no public constructor: " + plugin.getUserPluginClass().getName());
        } catch (IllegalAccessException e) {
            TwasiLogger.log.error("Abnormal UserPlugin: " + e.getMessage());
        }
    }

    /**
     * @param plugin The plugin to uninstall
     * @return whether it was uninstalled successfully
     */
    public boolean uninstallPlugin(TwasiPlugin plugin) {

        if (userPlugins.stream().noneMatch(userPlugin -> userPlugin.getClass().equals(plugin.getUserPluginClass()))) {
            TwasiLogger.log.info("Uninstall plugin " + plugin.getUserPluginClass() + " for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName() + " failed (not installed).");
            return false;
        }
        TwasiUserPlugin userPlugin = userPlugins.stream().filter(uPlugin -> uPlugin.getClass().equals(plugin.getUserPluginClass())).findFirst().get();

        LifecycleManagement.handleDisable(userPlugin);
        LifecycleManagement.handleUninstall(userPlugin);

        // Remove from db
        UserRepository userRepo = ServiceRegistry.get(DataService.class).get(UserRepository.class);
        User u = userRepo.getById(user.getId());
        u.getInstalledPlugins().remove(plugin.getName());
        userRepo.commit(u);

        userPlugins = userPlugins.stream().filter(uPlugin -> !uPlugin.getClass().equals(plugin.getUserPluginClass())).collect(Collectors.toList());
        return true;
    }

    public List<TwasiUserPlugin> getPlugins() {
        return userPlugins;
    }

    /**
     * @return all registered commands of all plugins
     */
    public List<TwasiCustomCommand> getCustomCommands() {
        List<TwasiCustomCommand> commands = new ArrayList<>();
        for (TwasiUserPlugin p : getPlugins())
            commands.addAll(p.getCommands());
        return commands.stream().filter(TwasiCustomCommand::allowsListing).collect(Collectors.toList());
    }

    /**
     * @param command the command's name without prefix
     * @return all UserPlugins that have registered a handler for the command
     */
    public List<TwasiUserPlugin> getByCommand(String command) {
        return userPlugins.stream().filter(
                plugin -> plugin.getCorePlugin().getDescription().handlesAllCommands() ||
                        plugin.getCorePlugin().getDescription().getCommands().stream().anyMatch(command::equalsIgnoreCase)
        ).collect(Collectors.toList());
    }

    /**
     * @return all UserPlugins that have registered a message handler
     */
    public List<TwasiUserPlugin> getMessagePlugins() {
        return userPlugins.stream().filter(plugin -> plugin.getCorePlugin().getDescription().handlesMessages()).collect(Collectors.toList());
    }
}
