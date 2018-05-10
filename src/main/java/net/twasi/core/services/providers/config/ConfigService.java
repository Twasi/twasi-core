package net.twasi.core.services.providers.config;

import net.twasi.core.services.IService;
import net.twasi.core.services.providers.config.catalog.ConfigCatalog;

public class ConfigService implements IService {
    private ConfigLoader loader;
    private ConfigCatalog catalog;

    public ConfigService() {
        loader = new ConfigLoader();
        catalog = loader.getConfigCatalog();
    }

    public ConfigCatalog getCatalog() {
        return catalog;
    }
}
