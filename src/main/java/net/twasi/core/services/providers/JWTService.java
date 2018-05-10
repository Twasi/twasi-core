package net.twasi.core.services.providers;

import net.twasi.core.webinterface.session.JWTManager;

public class JWTService {

    private static JWTManager service = new JWTManager();

    public static JWTManager getService() {
        return service;
    }

}
