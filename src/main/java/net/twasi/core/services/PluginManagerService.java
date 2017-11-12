package net.twasi.core.services;

import net.twasi.core.plugin.PluginManager;

public class PluginManagerService {

    public static PluginManager service = new PluginManager();

    public static PluginManager getService() {
        return service;
    }

}
