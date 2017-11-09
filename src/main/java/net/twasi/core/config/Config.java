package net.twasi.core.config;

import net.twasi.core.config.ConfigCatalog.ConfigCatalog;

public class Config {

    private static ConfigLoader loader;
    private static ConfigCatalog catalog;

    public static void load() {
        loader = new ConfigLoader();
        catalog = loader.configCatalog;
    }

    public static ConfigCatalog getCatalog() {
        return catalog;
    }

}
