package net.twasi.core.services.providers;

import net.twasi.core.application.AppState;
import net.twasi.core.services.IService;

public class AppStateService implements IService {

    private static AppState state = new AppState();

    public static AppState getService() {
        return state;
    }

}
