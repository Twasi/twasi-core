package net.twasi.core.services.providers;

import net.twasi.core.plugin.PluginManager;

public class PluginManagerService {

    private static PluginManager service = new PluginManager();

    public static PluginManager getService() {
        return service;
    }

}
