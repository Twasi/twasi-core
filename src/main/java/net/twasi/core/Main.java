package net.twasi.core;

import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.Plugin;

public class Main {

    public static void main(String[] args) {
        TwasiLogger.log.info("Starting Twasi Core");

        TwasiLogger.log.debug("Reading config");
        Config.load();

        TwasiLogger.log.debug("Loading plugins");
        Plugin.load();

        System.out.println(Config.catalog.database.hostname);
    }

}
