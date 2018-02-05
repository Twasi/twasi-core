package net.twasi.core.services;

import net.twasi.core.interfaces.twitch.webapi.TwitchAPI;

public class TwitchAPIService {

    private static TwitchAPI service = new TwitchAPI();

    /**
     * Get the current instance of the InstanceManager
     *
     * @return current InstanceManager
     */
    public static TwitchAPI getService() {
        return service;
    }

}
