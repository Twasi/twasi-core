package net.twasi.core.services.providers;

import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.webinterface.session.JWTManager;

public class JWTService implements IService {
    public static JWTService get(){
        return ServiceRegistry.get(JWTService.class);
    }

    private JWTManager service;

    public JWTService() {
        service = new JWTManager();
    }

    public JWTManager getManager() {
        return service;
    }

}
