package net.twasi.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import net.twasi.core.application.ApplicationState;
import net.twasi.core.cli.CommandLineInterface;
import net.twasi.core.graphql.WebInterfaceApp;
import net.twasi.core.interfaces.twitch.webapi.TwitchAPI;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.PluginDiscovery;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.*;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.services.providers.connector.TwitchConnectorService;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    private static CommandLineInterface cli = new CommandLineInterface();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        Logger root = (Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.OFF);

        TwasiLogger.log.info("Registering and starting services");
        ServiceRegistry.register(new AppStateService());
        ServiceRegistry.register(new ConfigService());
        ServiceRegistry.register(new DatabaseService());
        ServiceRegistry.register(new PluginManagerService());
        ServiceRegistry.register(new DataService());
        ServiceRegistry.register(new JWTService());
        ServiceRegistry.register(new InstanceManagerService());
        ServiceRegistry.register(new TwitchAPI());
        ServiceRegistry.register(new TwitchConnectorService());

        TwasiLogger.log.info("Starting Twasi Core");

        TwasiLogger.log.info("Connecting to database " + ServiceRegistry.get(ConfigService.class).getCatalog().database.hostname);
        ServiceRegistry.get(DatabaseService.class).connect();

        TwasiLogger.log.info("Preparing webinterface");
        WebInterfaceApp.prepare();

        // Establish connection to connector
        try {
            ServiceRegistry.get(TwitchConnectorService.class).connect();
        } catch (Exception e) {
            e.printStackTrace();
            TwasiLogger.log.error("Cannot connect to connector. Please fix this error and start again.");
            System.exit(1);
        }

        TwasiLogger.log.debug("Loading plugins");
        PluginDiscovery pd = new PluginDiscovery();
        pd.discoverAll();

        TwasiLogger.log.debug("Loading interfaces and joining active channels");
        ServiceRegistry.get(InstanceManagerService.class).startForAllUsers();

        float time = (float) (System.currentTimeMillis() - start);
        double longTime = time / 1000;
        TwasiLogger.log.info("Twasi ready. Started in " + longTime + " seconds.");
        AppStateService.getService().setState(ApplicationState.OPERATING);

        WebInterfaceApp.start();
        cli.start();
    }

}
