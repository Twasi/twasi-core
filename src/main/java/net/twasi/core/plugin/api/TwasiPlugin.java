package net.twasi.core.plugin.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.Command;
import net.twasi.core.models.Message.Message;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.translations.TwasiTranslation;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.util.List;

public abstract class TwasiPlugin implements TwasiPluginInterface {

    protected PluginConfig config;
    private TwasiTranslation translations;
    private HttpServer webServer;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onInstall(TwasiInterface inf) {

    }

    public void onUninstall(TwasiInterface inf) {

    }

    public void onCommand(Command command) {
        TwasiLogger.log.debug("Plugin '" + getConfig().getName() + "' has registered command '" + command.getCommandName() + "' but has no handler.");
    }

    public void onMessage(Message msg) {
        TwasiLogger.log.debug("Plugin '" + getConfig().getName() + "' has registered to messages events but has no handler.");
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
