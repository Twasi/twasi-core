package net.twasi.core.plugin.api;

import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
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
        User user = userPlugin.getTwasiInterface().getStreamer().getUser();
        userPlugin.onEnable(new TwasiEnableEvent(userPlugin));
        userPlugin.onInstall(new TwasiInstallEvent(userPlugin, user.getPermissionByName("twasi_user"), user.getPermissionByName("twasi_mod"), user.getPermissionByName("twasi_admin")));
        Database.getStore().save(user);
    }

    public static void handleUninstall(TwasiUserPlugin userPlugin) {
        User user = userPlugin.getTwasiInterface().getStreamer().getUser();
        userPlugin.onDisable(new TwasiDisableEvent(userPlugin));
        userPlugin.onUninstall(new TwasiInstallEvent(userPlugin, user.getPermissionByName("twasi_user"), user.getPermissionByName("twasi_mod"), user.getPermissionByName("twasi_admin")));
        Database.getStore().save(user);
    }

    public static void handleEnable(TwasiUserPlugin plugin) {
        plugin.onEnable(new TwasiEnableEvent(plugin));
    }

    public static void handleDisable(TwasiUserPlugin plugin) {
        plugin.onDisable(new TwasiDisableEvent(plugin));
    }
}
