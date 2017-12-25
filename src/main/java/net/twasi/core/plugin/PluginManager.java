package net.twasi.core.plugin;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.services.InstanceManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PluginManager {

    private List<TwasiPlugin> plugins = new ArrayList<>();

    /**
     * Registers a plugin and calls it's install events.
     * Does check that plugins aren't registerd twice.
     * @param plugin The plugin to register
     * @return if the plugin was registered successfully
     */
    public boolean registerPlugin(TwasiPlugin plugin) {
        if (plugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to register plugin " + plugin.getConfig().getName() + " twice. Skipped.");
            return false;
        }

        plugins.add(plugin);
        plugin.onEnable();

        // TODO if plugin enabling/disabling system is enabled this should be checked here
        // all plugins will be enabled for everyone until then
        for(TwasiInterface twasiInterface : InstanceManagerService.getService().getInterfaces()) {
            try {
                plugin.onInstall(twasiInterface);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
                e.printStackTrace();
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
