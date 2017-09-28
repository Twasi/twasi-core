package net.twasi.core.webinterface.api;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.api.User.ApiUserHandler;

public class ApiRegistry {

    public static void register(HttpServer server) {
        // User
        server.createContext("/api/user", new ApiUserHandler());
    }

}
