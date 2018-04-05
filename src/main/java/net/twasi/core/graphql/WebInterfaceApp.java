package net.twasi.core.graphql;

import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.webinterface.metrics.CustomMetrics;
import net.twasi.core.webinterface.registry.AuthRegistry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.IOException;

public class WebInterfaceApp {

    private static Server server;
    private static CustomMetrics metrics;

    public static void prepare() {
        try {
            // Create server
            // server = HttpServer.create(new InetSocketAddress(Config.getCatalog().webinterface.port), 0);
            server = new Server(Config.getCatalog().webinterface.port);

            // Create server for metrics
            HTTPServer metricsServer = new HTTPServer("0.0.0.0", Config.getCatalog().webinterface.metricsPort);

            metrics = new CustomMetrics();
            DefaultExports.initialize();

            HandlerCollection handlers = new HandlerCollection();
            server.setHandler(handlers);

            // Register all handlers
            handlers.addHandler(AuthRegistry.register());
            //AuthRegistry.register(server);
            //ConfirmRegistry.register(server);

            /* ContextHandler graphQLContext = new ContextHandler();
            graphQLContext.setContextPath("/graphql/");
            graphQLContext.setHandler(new GraphQLEndpoint());

            server.setHandler(graphQLContext);*/

            ServletHandler servletHandler = new ServletHandler();
            handlers.addHandler(servletHandler);

            servletHandler.addServletWithMapping(GraphQLEndpoint.class, "/graphql/*");

            // Handle all other request static
            // server.createContext("/", new NotFoundController());
        } catch (IOException e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
        }
    }

    public static void start() {
        // Start server, show message
        try {
            server.start();
            //server.join();
            TwasiLogger.log.info("Web interface started on port " + Config.getCatalog().webinterface.port);
        } catch (Exception e) {
            TwasiLogger.log.error("Error while starting webserver", e);
        }
    }

    public static Server getServer() {
        return server;
    }


}
