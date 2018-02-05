package net.twasi.core.interfaces.api;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.LifecycleManagement;
import net.twasi.core.plugin.api.TwasiUserPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TwasiInterface implements TwasiInterfaceInterface {

    private List<TwasiUserPlugin> userPlugins = new ArrayList<>();

    /**
     * Installs a certain user plugin for the given user
     *
     * @param plugin The plugin to install
     * @return if it was installed successfully
     */
    public boolean installPlugin(TwasiPlugin plugin) {
        if (userPlugins.stream().anyMatch(userPlugin -> userPlugin.getClass().equals(plugin.getUserPluginClass()))) {
            TwasiLogger.log.info("Tried to install userplugin " + plugin.getUserPluginClass() + " twice for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName());
            return false;
        }

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

    public boolean uninstallPlugin(TwasiPlugin plugin) {
        if (userPlugins.stream().noneMatch(userPlugin -> userPlugin.getClass().equals(plugin.getUserPluginClass()))) {
            TwasiLogger.log.info("Uninstall plugin " + plugin.getUserPluginClass() + " for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName() + " failed (not installed).");
            return false;
        }
        TwasiUserPlugin userPlugin = userPlugins.stream().filter(uPlugin -> uPlugin.getClass().equals(plugin.getUserPluginClass())).findFirst().get();

        LifecycleManagement.handleUninstall(userPlugin);

        userPlugins = userPlugins.stream().filter(uPlugin -> !userPlugin.getClass().equals(plugin.getUserPluginClass())).collect(Collectors.toList());
        return true;
    }

    public List<TwasiUserPlugin> getPlugins() {
        return userPlugins;
    }

    public List<TwasiUserPlugin> getByCommand(String command) {
        return userPlugins.stream().filter(
                plugin -> plugin.getCorePlugin().getDescription().getCommands().stream().map(
                        String::toLowerCase).collect(Collectors.toList()
                ).contains(command.toLowerCase())
        ).collect(Collectors.toList());
    }

    public List<TwasiUserPlugin> getMessagePlugins() {
        return userPlugins.stream().filter(plugin -> plugin.getCorePlugin().getDescription().handlesMessages()).collect(Collectors.toList());
    }
}
