package net.twasi.core.webinterface.registry;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.controller.InfoController;
import net.twasi.core.webinterface.controller.VersionController;
import net.twasi.core.webinterface.controller.bot.BotInfoController;
import net.twasi.core.webinterface.controller.bot.StartController;
import net.twasi.core.webinterface.controller.bot.StopController;
import net.twasi.core.webinterface.controller.plugins.PluginController;
import net.twasi.core.webinterface.controller.user.UserController;
import net.twasi.core.webinterface.controller.user.UserEventsController;
import net.twasi.core.webinterface.controller.user.UserRefreshController;

public class ApiRegistry {

    public static void register(HttpServer server) {

        // Info
        server.createContext("/api", new InfoController());

        // Version
        server.createContext("/api/version", new VersionController());

        // User
        server.createContext("/api/user", new UserController());

        // Refresh
        server.createContext("/api/user/refresh", new UserRefreshController());
        // Events
        server.createContext("/api/user/events", new UserEventsController());

        // Bot
        server.createContext("/api/bot", new BotInfoController());
        server.createContext("/api/bot/start", new StartController());
        server.createContext("/api/bot/stop", new StopController());

        // Plugins
        server.createContext("/api/plugins", new PluginController());
    }

}
