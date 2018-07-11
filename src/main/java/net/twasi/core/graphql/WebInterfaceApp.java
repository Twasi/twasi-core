package net.twasi.core.graphql;

import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.webinterface.metrics.CustomMetrics;
import net.twasi.core.webinterface.registry.AuthRegistry;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class WebInterfaceApp {

    private static Server server;
    private static CustomMetrics metrics;
    private static HandlerCollection handlers = new HandlerCollection();
    private static ServletContextHandler context = new ServletContextHandler();

    /* public static void prepare() {
        try {
            // Create server
            // server = HttpServer.create(new InetSocketAddress(Config.getCatalog().webinterface.port), 0);
            server = new Server(ServiceRegistry.get(ConfigService.class).getCatalog().webinterface.port);

            // Create server for metrics
            // HTTPServer metricsServer = new HTTPServer("0.0.0.0", ServiceRegistry.get(ConfigService.class).getCatalog().webinterface.metricsPort);

            metrics = new CustomMetrics();
            DefaultExports.initialize();

            server.setHandler(handlers);

            // Register all handlers
            handlers.addHandler(AuthRegistry.register());
            //AuthRegistry.register(server);
            //ConfirmRegistry.register(server);

            /* ContextHandler graphQLContext = new ContextHandler();
            graphQLContext.setContextPath("/graphql/");
            graphQLContext.setHandler(new GraphQLEndpoint());

            server.setHandler(graphQLContext);
            handlers.addHandler(servletHandler);

            TwasiLogger.log.debug("Registered GraphQL Endpoint to Webserver");
            //servletHandler.addServletWithMapping(GraphQLEndpoint.class, "/graphql/*");
            //servletHandler.addServletWithMapping(GraphQLEndpoint.class, "/another/*");

            // Handle all other request static
            // server.createContext("/", new NotFoundController());
        } catch (IOException e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
        }
    }*/

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
