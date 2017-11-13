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

    public boolean registerPlugin(TwasiPlugin plugin) {
        if (plugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to register plugin " + plugin.getConfig().getName() + " twice. Skipped.");
            return false;
        }

        plugins.add(plugin);
        plugin.onEnable();

        // TODO if plugin enabling/disabling system is enabled this should be checked here
        // all plugins will be enabled for all until then
        for(TwasiInterface twasiInterface : InstanceManagerService.getService().interfaces) {
            plugin.onInstall(twasiInterface);
        }

        return true;
    }

    public List<TwasiPlugin> getPlugins() {
        return plugins;
    }

    public List<TwasiPlugin> getByCommand(String command) {
        List<TwasiPlugin> availablePlugins = plugins.stream().filter(
                plugin -> plugin.getConfig().getCommands().stream().map(
                        c -> c.toLowerCase()).collect(Collectors.toList()
                ).contains(command.toLowerCase())
        ).collect(Collectors.toList());
        return availablePlugins;
    }

    public List<TwasiPlugin> getMessagePlugins() {
        return plugins.stream().filter(plugin -> plugin.getConfig().handlesMessages()).collect(Collectors.toList());
    }
}
