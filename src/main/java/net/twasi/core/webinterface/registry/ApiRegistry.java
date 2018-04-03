package net.twasi.core.webinterface.registry;

import net.twasi.core.webinterface.controller.VersionController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class ApiRegistry {

    public static HandlerCollection register() {
        HandlerCollection col = new ContextHandlerCollection();

        // Info
        /* ContextHandler infoContext = new ContextHandler();
        infoContext.setContextPath("/api");
        infoContext.setHandler(new InfoController());
        server.setHandler(infoContext); */

        // Version
        ContextHandler versionContext = new ContextHandler();
        versionContext.setContextPath("/api/version");
        versionContext.setHandler(new VersionController());
        col.addHandler(versionContext);

        // Settings
        // server.createContext("/api/settings", new SettingsController());

        // Refresh
        // server.createContext("/api/user/refresh", new UserRefreshController());
        // Events
        // server.createContext("/api/user/events", new UserEventsController());

        // Bot
        // server.createContext("/api/bot", new BotInfoController());
        // server.createContext("/api/bot/start", new StartController());
        // server.createContext("/api/bot/stop", new StopController());

        // Plugins
        // server.createContext("/api/plugins", new PluginController());
        return col;
    }

}
