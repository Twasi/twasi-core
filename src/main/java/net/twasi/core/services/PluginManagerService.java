package net.twasi.core.services;

import net.twasi.core.plugin.PluginManager;

public class PluginManagerService {

    private static PluginManager service = new PluginManager();

    public static PluginManager getService() {
        return service;
    }

}
