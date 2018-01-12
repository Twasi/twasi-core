package net.twasi.core;

import net.twasi.core.application.AppState;
import net.twasi.core.application.ApplicationState;
import net.twasi.core.cli.CommandLineInterface;
import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.Plugin;
import net.twasi.core.plugin.PluginDiscovery;
import net.twasi.core.plugin.PluginLoader;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.java.JavaPluginLoader;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.services.mail.MailService;
import net.twasi.core.translations.TwasiTranslation;
import net.twasi.core.webinterface.WebInterfaceApp;

import java.io.File;

public class Main {

    private static CommandLineInterface cli = new CommandLineInterface();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        TwasiLogger.log.info("Starting Twasi Core");

        TwasiLogger.log.debug("Reading config");
        Config.load();

        TwasiLogger.log.info("Connecting to database " + Config.getCatalog().database.hostname);
        Database.connect();

        TwasiLogger.log.info("Preparing webinterface");
        WebInterfaceApp.prepare();

        TwasiLogger.log.debug("Loading interfaces and joining active channels");
        InstanceManagerService.getService().startForAllUsers(UserStore.getUsers());

        TwasiLogger.log.debug("Loading plugins");
        PluginDiscovery pd = new PluginDiscovery();
        pd.discoverAll();

        float time = (float) (System.currentTimeMillis() - start);
        double longTime = time / 1000;
        TwasiLogger.log.info("Twasi ready. Started in " + longTime + " seconds.");
        AppState.setState(ApplicationState.OPERATING);

        WebInterfaceApp.start();
        cli.start();
    }

}
