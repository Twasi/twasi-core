package net.twasi.core.services.providers;

import net.twasi.core.application.AppState;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;

public class AppStateService implements IService {

    public static AppStateService get() {
        return ServiceRegistry.get(AppStateService.class);
    }

    private static AppState state = new AppState();

    public static AppState getService() {
        return state;
    }

}
