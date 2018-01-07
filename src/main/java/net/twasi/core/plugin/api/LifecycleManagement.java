package net.twasi.core.plugin.api;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.events.TwasiDisableEvent;
import net.twasi.core.plugin.api.events.TwasiEnableEvent;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasi.core.plugin.api.events.TwasiUninstallEvent;
import net.twasi.core.translations.TwasiTranslation;

public class LifecycleManagement {

    public static void initiate(TwasiUserPlugin userPlugin, TwasiInterface inf, TwasiPlugin plugin) {
        userPlugin.setTwasiInterface(inf);
        userPlugin.setCorePlugin(plugin);
        userPlugin.setTranslations(new TwasiTranslation(plugin.getClassLoader()));
    }

    public static void handleInstall(TwasiUserPlugin userPlugin) {
        userPlugin.onEnable(new TwasiEnableEvent(userPlugin));
        userPlugin.onInstall(new TwasiInstallEvent(userPlugin));
    }

    public static void handleUninstall(TwasiUserPlugin plugin) {
        plugin.onDisable(new TwasiDisableEvent(plugin));
        plugin.onUninstall(new TwasiUninstallEvent(plugin));
    }

    public static void handleEnable(TwasiUserPlugin plugin) {
        plugin.onEnable(new TwasiEnableEvent(plugin));
    }

    public static void handleDisable(TwasiUserPlugin plugin) {
        plugin.onDisable(new TwasiDisableEvent(plugin));
    }
}
