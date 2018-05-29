package net.twasi.core.services.providers;

import net.twasi.core.services.IService;
import net.twasi.core.webinterface.session.JWTManager;

public class JWTService implements IService {
    private JWTManager service;

    public JWTService() {
        service = new JWTManager();
    }

    public JWTManager getManager() {
        return service;
    }

}
