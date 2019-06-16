package net.twasi.core.services.providers;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plugin manager does manage the lifecycle of all plugins
 */
public class PluginManagerService implements IService {

    public static PluginManagerService get() {
        return ServiceRegistry.get(PluginManagerService.class);
    }

    private List<TwasiPlugin> plugins = new ArrayList<>();
    private List<TwasiDependency> dependencies = new ArrayList<>();

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

    public boolean registerDependency(TwasiDependency dependency) {
        if (dependencies.contains(dependency)) {
            TwasiLogger.log.info("Tried to register dependency " + dependency.getDescription().getName() + " twice. Skipped.");
            return false;
        }
        dependencies.add(dependency);
        return true;
    }

    public List<TwasiPlugin> getPlugins() {
        return plugins;
    }

    public List<TwasiDependency> getDependencies() {
        return dependencies;
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
     *
     * @param name the name to search
     * @return the corresponding plugin, or null if not found
     */
    public TwasiPlugin getByName(String name) {
        return plugins.stream().filter(plugin -> plugin.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Returns a dependency by a name
     *
     * @param name the name to search
     * @return the corresponding plugin, or null if not found
     */
    public TwasiDependency getDependencyByName(String name) {
        return dependencies.stream().filter(dep -> dep.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void callReady() {
        plugins.forEach(TwasiPlugin::onReady);
        dependencies.forEach(TwasiPlugin::onReady);
    }

    /**
     * Returns all plugins that are not hidden and should be installed by default
     *
     * @return plugins that should be preinstalled
     */
    public List<String> getDefaultPlugins() {
        return plugins.stream().filter(p ->
                !p.getDescription().isHidden() && p.getDescription().isAutoInstall()
        ).map(p -> p.getDescription().getName()).collect(Collectors.toList());
    }
}
