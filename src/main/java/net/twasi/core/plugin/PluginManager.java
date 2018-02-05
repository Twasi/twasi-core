package net.twasi.core.plugin;

import net.twasi.core.database.store.UserStore;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.InstanceManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plugin manager does manage the lifecycle of all plugins
 */
public class PluginManager {

    private List<TwasiPlugin> plugins = new ArrayList<>();

    /**
     * Registers a plugin
     * Does check that plugins aren't registerd twice.
     * Afterwards enables it for all installed instances.
     *
     * @param plugin The plugin to register
     * @return if the plugin was registered successfully
     */
    boolean registerPlugin(TwasiPlugin plugin) {
        if (plugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to register plugin " + plugin.getDescription().getName() + " twice. Skipped.");
            return false;
        }
        plugins.add(plugin);

        UserStore.getUsers().stream().filter(user -> user.getInstalledPlugins().contains(plugin.getName())).parallel().forEach(user -> {
            TwasiInterface inf = InstanceManagerService.getService().getByUser(user);
            inf.enableUserPlugin(plugin);
        });
        return true;
    }

    public List<TwasiPlugin> getPlugins() {
        return plugins;
    }

    /**
     * Returns all plugins which have registered the command
     *
     * @param command The command to search plugins for
     * @return a list of all plugins matching (empty if none)
     */
    public List<TwasiPlugin> getByCommand(String command) {
        return plugins.stream().filter(
                plugin -> plugin.getDescription().getCommands().stream().map(
                        String::toLowerCase).collect(Collectors.toList()
                ).contains(command.toLowerCase())
        ).collect(Collectors.toList());
    }

    /**
     * Returns all plugins which have subscribed to the message event
     *
     * @return a list of all plugins matching (empty if none)
     */
    public List<TwasiPlugin> getMessagePlugins() {
        return plugins.stream().filter(plugin -> plugin.getDescription().handlesMessages()).collect(Collectors.toList());
    }
}
