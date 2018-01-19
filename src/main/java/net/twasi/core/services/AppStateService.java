package net.twasi.core.services;

import net.twasi.core.application.AppState;

public class AppStateService {

    private static AppState state = new AppState();

    public static AppState getService() {
        return state;
    }

}
