package net.twasi.core.webinterface.registry;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.controller.user.UserController;

public class ApiRegistry {

    public static void register(HttpServer server) {
        // User
        server.createContext("/api/user", new UserController());
    }

}
