package net.twasi.core.webinterface;

import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.webinterface.lib.NotFoundController;
import net.twasi.core.webinterface.metrics.CustomMetrics;
import net.twasi.core.webinterface.registry.ApiRegistry;
import net.twasi.core.webinterface.registry.AuthRegistry;
import net.twasi.core.webinterface.registry.ConfirmRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebInterfaceApp {

    private static HttpServer server;
    private static CustomMetrics metrics;

    public static void prepare() {
        try {
            // Create server
            server = HttpServer.create(new InetSocketAddress(Config.getCatalog().webinterface.port), 0);

            // Create server for metrics
            HTTPServer metricsServer = new HTTPServer("0.0.0.0", Config.getCatalog().webinterface.metricsPort);

            metrics = new CustomMetrics();
            DefaultExports.initialize();

            // Register all handlers
            ApiRegistry.register(server);
            AuthRegistry.register(server);
            ConfirmRegistry.register(server);

            // Handle all other request static
            server.createContext("/", new NotFoundController());
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
