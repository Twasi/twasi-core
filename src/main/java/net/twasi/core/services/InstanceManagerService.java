package net.twasi.core.services;

import net.twasi.core.instances.InstanceManager;

public class InstanceManagerService {

    private static InstanceManager service = new InstanceManager();

    /**
     * Get the current instance of the InstanceManager
     * @return current InstanceManager
     */
    public static InstanceManager getService() {
        return service;
    }
}
