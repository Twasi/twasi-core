package net.twasi.core.plugin.api;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.api.events.*;
import net.twasi.core.translations.TwasiTranslation;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.util.List;

public abstract class TwasiPlugin implements TwasiPluginInterface {

    private PluginConfig config;
    private TwasiTranslation translations;
    private HttpServer webServer;

    public void onActivate(TwasiActivateEvent e) {}

    public void onDeactivate(TwasiDeactivateEvent e) {}

    public void onEnable(TwasiEnableEvent e) {}

    public void onDisable(TwasiDisableEvent e) {}

    public void onInstall(TwasiInstallEvent e) {}

    public void onUninstall(TwasiUninstallEvent e) {}

    public void onCommand(TwasiCommandEvent e) {
        TwasiLogger.log.debug("Plugin '" + getConfig().getName() + "' has registered command '" + e.getCommand().getCommandName() + "' but has no handler.");
    }

    public void onMessage(TwasiMessageEvent e) {
        TwasiLogger.log.debug("Plugin '" + getConfig().getName() + "' has registered to message events but has no handler.");
    }

    public void setConfig(PluginConfig config) {
        this.config = config;
    }

    public PluginConfig getConfig() {
        return config;
    }

    public List<String> getRegisteredCommands() {
        return null;
    }

    public void setTranslations(TwasiTranslation translations) {
        this.translations = translations;
    }

    public TwasiTranslation getTranslations() {
        return translations;
    }

    public void registerWebHandler(String path, RequestHandler handler) {
        webServer.createContext(path, handler);
    }

    public void setWebServer(HttpServer webServer) {
        this.webServer = webServer;
    }
}
