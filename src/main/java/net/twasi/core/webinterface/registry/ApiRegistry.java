package net.twasi.core.webinterface.registry;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.controller.InfoController;
import net.twasi.core.webinterface.controller.bot.BotInfoController;
import net.twasi.core.webinterface.controller.bot.StartController;
import net.twasi.core.webinterface.controller.bot.StopController;
import net.twasi.core.webinterface.controller.user.UserController;
import net.twasi.core.webinterface.controller.user.UserEventsController;
import net.twasi.core.webinterface.controller.user.UserRefreshController;

public class ApiRegistry {

    public static void register(HttpServer server) {

        // Info
        server.createContext("/api", new InfoController());

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
    }

}
