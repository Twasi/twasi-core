package net.twasi.core;

import net.twasi.core.application.ApplicationState;
import net.twasi.core.cli.CommandLineInterface;
import net.twasi.core.api.WebInterfaceApp;
import net.twasi.core.logger.JettyVoidLogger;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.PluginDiscovery;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.*;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.twitchapi.TwitchAPI;
import net.twasi.twitchapi.auth.AuthorizationContext;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.logging.Logger;

public class Main {

    private static CommandLineInterface cli = new CommandLineInterface();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        TwasiLogger.log.info("Registering and starting services");
        ServiceRegistry.register(new AppStateService());
        ServiceRegistry.register(new ConfigService());
        ServiceRegistry.register(new DatabaseService());
        ServiceRegistry.register(new PluginManagerService());
        ServiceRegistry.register(new DataService());
        ServiceRegistry.register(new JWTService());
        ServiceRegistry.register(new InstanceManagerService());
        ServiceRegistry.register(new ApiSchemaManagementService());
        ApiContextInitializer.init(); // Init Telegram API context from a static block
        ServiceRegistry.register(new TelegramService());
        ServiceRegistry.register(new WebsocketService());

        TwitchAPI.initialize(new AuthorizationContext(
                ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientId,
                ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientSecret,
                ServiceRegistry.get(ConfigService.class).getCatalog().twitch.redirectUri
        ));

        // Disable logging
        org.eclipse.jetty.util.log.Log.setLog(new JettyVoidLogger());
        LogManager.getLogger("graphql").setLevel(Level.OFF);
        LogManager.getLogger("org.pircbotx").setLevel(Level.WARN);
        Logger.getLogger("org.mongodb.driver").setLevel(java.util.logging.Level.SEVERE);

        TwasiLogger.log.info("Starting Twasi Core");

        TwasiLogger.log.info("Connecting to database " + ServiceRegistry.get(ConfigService.class).getCatalog().database.hostname);
        ServiceRegistry.get(DatabaseService.class).connect();

        TwasiLogger.log.info("Preparing webinterface");
        WebInterfaceApp.prepare();

        TwasiLogger.log.debug("Loading plugins");
        PluginDiscovery pd = new PluginDiscovery();
        pd.discoverAll();

        TwasiLogger.log.debug("Loading interfaces and joining active channels");
        ServiceRegistry.get(InstanceManagerService.class).startForAllUsers();

        float time = (float) (System.currentTimeMillis() - start);
        double longTime = time / 1000;
        TwasiLogger.log.info("Twasi ready. Started in " + longTime + " seconds.");
        AppStateService.getService().setState(ApplicationState.OPERATING);

        // Call plugins/dependencies ready
        ServiceRegistry.get(PluginManagerService.class).callReady();

        WebInterfaceApp.start();
        cli.start();
    }

}
