package net.twasi.core.interfaces.api;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiInterface implements TwasiInterfaceInterface {

    public List<TwasiPlugin> installedPlugins = new ArrayList<>();

    public boolean installPlugin(TwasiPlugin plugin) {
        if (installedPlugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to install plugin " + plugin.getConfig().getName() + " twice for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName());
            return false;
        }
        plugin.onInstall(this);
        installedPlugins.add(plugin);
        return true;
    }

    public boolean uninstallPlugin(TwasiPlugin plugin) {
        if (!installedPlugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to uninstall plugin " + plugin.getConfig().getName() + " for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName() + " failed (not installed).");
            return false;
        }
        plugin.onUninstall(this);
        installedPlugins.remove(plugin);
        return true;
    }
}
