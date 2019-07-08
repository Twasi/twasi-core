package net.twasi.core.api;

import io.prometheus.client.exporter.MetricsServlet;
import net.twasi.core.graphql.GraphQLEndpoint;
import net.twasi.core.api.oauth.OAuthIntegrationController;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.webinterface.controller.auth.AuthCallbackController;
import net.twasi.core.webinterface.controller.auth.AuthController;
import net.twasi.core.webinterface.metrics.CustomMetrics;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebInterfaceApp {

    private static Server server;
    private static CustomMetrics metrics;
    private static HandlerCollection handlers = new HandlerCollection();
    private static ServletContextHandler context = new ServletContextHandler();
    private static int port = ServiceRegistry.get(ConfigService.class).getCatalog().webinterface.port;
    private static OAuthIntegrationController oAuthIntegrationController = new OAuthIntegrationController();

    public static void prepare() {
        server = new Server(port);

        context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(GraphQLEndpoint.class, "/graphql");

        handlers.addHandler(context);

        server.setHandler(handlers);

        context.addServlet(AuthController.class, "/auth");
        context.addServlet(AuthCallbackController.class, "/auth/callback");

        context.addServlet(new ServletHolder(oAuthIntegrationController), "/oauth/*");

        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
    }

    public static void start() {
        // Start server, show message
        try {
            server.start();
            TwasiLogger.log.info("Web interface started on port " + port);
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
