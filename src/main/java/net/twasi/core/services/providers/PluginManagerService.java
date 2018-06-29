package net.twasi.core.services.providers;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.IService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plugin manager does manage the lifecycle of all plugins
 */
public class PluginManagerService implements IService {

    private List<TwasiPlugin> plugins = new ArrayList<>();

    /**
     * Registers a plugin
     * Does check that plugins aren't registerd twice.
     * Afterwards enables it for all installed instances.
     *
     * @param plugin The plugin to register
     * @return if the plugin was registered successfully
     */
    public boolean registerPlugin(TwasiPlugin plugin) {
        if (plugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to register plugin " + plugin.getDescription().getName() + " twice. Skipped.");
            return false;
        }
        plugins.add(plugin);

        // TODO to support plugin registration at runtime
        /* UserStore.getUsers().stream().filter(user -> user.getInstalledPlugins().contains(plugin.getName())).parallel().forEach(user -> {
            TwasiInterface inf = InstanceManagerService.getService().getByUser(user);
            inf.enableUserPlugin(plugin);
        });
        return true; */
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

    /**
     * Returns a plugin by a name
     * @param name the name to search
     * @return the corresponding plugin, or null if not found
     */
    public TwasiPlugin getByName(String name) {
        return plugins.stream().filter(plugin -> plugin.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
