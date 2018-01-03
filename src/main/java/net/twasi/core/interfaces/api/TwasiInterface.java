package net.twasi.core.interfaces.api;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasi.core.plugin.api.events.TwasiUninstallEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiInterface implements TwasiInterfaceInterface {

    private List<TwasiPlugin> enabledPlugins = new ArrayList<>();

    public boolean installPlugin(TwasiPlugin plugin) {
        if (enabledPlugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to install plugin " + plugin.getConfig().getName() + " twice for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName());
            return false;
        }
        plugin.onInstall(new TwasiInstallEvent(this));
        enabledPlugins.add(plugin);
        return true;
    }

    public boolean disablePlugin(TwasiPlugin plugin) {
        if (!enabledPlugins.contains(plugin)) {
            TwasiLogger.log.info("Tried to uninstall plugin " + plugin.getConfig().getName() + " for Streamer " + getStreamer().getUser().getTwitchAccount().getUserName() + " failed (not installed).");
            return false;
        }
        plugin.onUninstall(new TwasiUninstallEvent(this));
        enabledPlugins.remove(plugin);
        return true;
    }
}
