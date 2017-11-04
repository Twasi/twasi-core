package net.twasi.core.webinterface.registry;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.controller.auth.AuthCallbackController;
import net.twasi.core.webinterface.controller.auth.AuthController;

public class AuthRegistry {

    public static void register(HttpServer server) {
        // Authenticate
        server.createContext("/auth", new AuthController());

        // Callback
        server.createContext("/auth/callback", new AuthCallbackController());
    }

}
