package net.twasi.core.plugin.api;

import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.events.TwasiDisableEvent;
import net.twasi.core.plugin.api.events.TwasiEnableEvent;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.translations.TwasiTranslation;

public class LifecycleManagement {

    public static void initiate(TwasiUserPlugin userPlugin, TwasiInterface inf, TwasiPlugin plugin) {
        userPlugin.setTwasiInterface(inf);
        userPlugin.setCorePlugin(plugin);
        userPlugin.setTranslations(new TwasiTranslation(plugin.getClassLoader()));
    }

    public static void handleInstall(TwasiUserPlugin userPlugin) {
        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getById(userPlugin.getTwasiInterface().getStreamer().getUser().getId());
        userPlugin.onEnable(new TwasiEnableEvent(userPlugin));
        userPlugin.onInstall(new TwasiInstallEvent(userPlugin, user.getPermissionByName("twasi_user"), user.getPermissionByName("twasi_mod"), user.getPermissionByName("twasi_admin")));
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);
    }

    public static void handleUninstall(TwasiUserPlugin userPlugin) {
        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getById(userPlugin.getTwasiInterface().getStreamer().getUser().getId());
        userPlugin.onDisable(new TwasiDisableEvent(userPlugin));
        userPlugin.onUninstall(new TwasiInstallEvent(userPlugin, user.getPermissionByName("twasi_user"), user.getPermissionByName("twasi_mod"), user.getPermissionByName("twasi_admin")));
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);
    }

    public static void handleEnable(TwasiUserPlugin plugin) {
        plugin.onEnable(new TwasiEnableEvent(plugin));
    }

    public static void handleDisable(TwasiUserPlugin plugin) {
        plugin.onDisable(new TwasiDisableEvent(plugin));
    }
}
