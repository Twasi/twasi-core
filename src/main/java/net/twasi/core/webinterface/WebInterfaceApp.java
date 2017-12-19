package net.twasi.core.webinterface;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.webinterface.registry.ApiRegistry;
import net.twasi.core.webinterface.registry.AuthRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebInterfaceApp {

    private static HttpServer server;

    public static void prepare() {
        try {
            // Create server
            server = HttpServer.create(new InetSocketAddress(Config.getCatalog().webinterface.port), 0);

            // Register all handlers
            ApiRegistry.register(server);
            AuthRegistry.register(server);

            // Handle all other request static
            // server.createContext("/", new StaticHandler());
        } catch (IOException e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
        }
    }

    public static void start() {
        // Start server, show message
        server.start();
        TwasiLogger.log.info("Web interface started on port " + Config.getCatalog().webinterface.port);
    }

    public static HttpServer getServer() {
        return server;
    }


}
