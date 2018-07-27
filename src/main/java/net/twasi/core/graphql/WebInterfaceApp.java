package net.twasi.core.graphql;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.webinterface.metrics.CustomMetrics;
import net.twasi.core.webinterface.registry.AuthRegistry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class WebInterfaceApp {

    private static Server server;
    private static CustomMetrics metrics;
    private static HandlerCollection handlers = new HandlerCollection();
    private static ServletContextHandler context = new ServletContextHandler();

    public static void prepare() {
        server = new Server(ServiceRegistry.get(ConfigService.class).getCatalog().webinterface.port);

        context = new ServletContextHandler();
        context.setContextPath("/apis");
        context.addServlet(GraphQLEndpoint.class, "/");

        handlers.addHandler(context);
        handlers.addHandler(AuthRegistry.register());

        server.setHandler(handlers);
    }

    public static void start() {
        // Start server, show message
        try {
            server.start();
            TwasiLogger.log.info("Web interface started on port " + ServiceRegistry.get(ConfigService.class).getCatalog().webinterface.port);
        } catch (Exception e) {
            TwasiLogger.log.error("Error while starting webserver", e);
        }
    }

    public static Server getServer() {
        return server;
    }

    public static HandlerCollection getHandlers() {
        return handlers;
    }

    public static ServletContextHandler getServletHandler() { return context; }
}
