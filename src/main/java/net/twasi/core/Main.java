package net.twasi.core;

import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;

public class Main {

    public static void main(String[] args) {
        TwasiLogger.log.info("Starting Twasi Core");

        TwasiLogger.log.debug("Reading Config");
        Config.load();

        System.out.println(Config.catalog.database.hostname);
    }

}
