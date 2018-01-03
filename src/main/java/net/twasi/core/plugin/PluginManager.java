package net.twasi.core.plugin;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.plugin.api.events.TwasiEnableEvent;
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
     * Registers a plugin and calls it's install events.
     * Does check that plugins aren't registerd twice.
     * Afterwards enables it for all installed instances.
     * @param plugin The plugin to register
     * @return if the plugin was registered successfully
     */
    public boolean registerPlugin(TwasiPlugin plugin) {
        if (plugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to register plugin " + plugin.getConfig().getName() + " twice. Skipped.");
            return false;
        }

        plugins.add(plugin);

        for(TwasiInterface twasiInterface : InstanceManagerService.getService().getInterfaces()) {
            if (twasiInterface.getStreamer().getUser().getInstalledPlugins().contains(plugin.getConfig().getName())) {
                // It is installed, call onEnable
                plugin.onEnable(new TwasiEnableEvent(twasiInterface));
            }
        }

        return true;
    }

    public List<TwasiPlugin> getPlugins() {
        return plugins;
    }

    /**
     * Returns all plugins which have registered the command
     * @param command The command to search plugins for
     * @return a list of all plugins matching (empty if none)
     */
    public List<TwasiPlugin> getByCommand(String command) {
        return plugins.stream().filter(
                plugin -> plugin.getConfig().getCommands().stream().map(
                        String::toLowerCase).collect(Collectors.toList()
                ).contains(command.toLowerCase())
        ).collect(Collectors.toList());
    }

    /**
     * Returns all plugins which have subscribed to the message event
     * @return a list of all plugins matching (empty if none)
     */
    public List<TwasiPlugin> getMessagePlugins() {
        return plugins.stream().filter(plugin -> plugin.getConfig().handlesMessages()).collect(Collectors.toList());
    }
}
