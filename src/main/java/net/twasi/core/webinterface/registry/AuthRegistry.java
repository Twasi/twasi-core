package net.twasi.core.webinterface.registry;

import com.sun.net.httpserver.HttpServer;

public class AuthRegistry {

    public static void register(HttpServer server) {
        // Authenticate
        // server.createContext("/auth", new AuthController());

        // Callback
        // server.createContext("/auth/callback", new AuthCallbackController());
    }

}
