package net.twasi.core.services;

import net.twasi.core.webinterface.session.JWTManager;

public class JWTService {

    public static JWTManager service = new JWTManager();

    public static JWTManager getService() {
        return service;
    }

}
