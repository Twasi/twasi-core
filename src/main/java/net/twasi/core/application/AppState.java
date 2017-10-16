package net.twasi.core.application;

/**
 * Static class that holds and manages the state of the application.
 */
public class AppState {

    /**
     * Current state of the application
     */
    public static ApplicationState state = ApplicationState.STARTING;

    /**
     * Checks if the application is starting. If this is the case, no changes or operations should be accepted.
     * @return if the application is starting up
     */
    public static boolean isStarting() {
        return state == ApplicationState.STARTING;
    }

    /**
     * Checks if the application is operating and ready for further operations
     * @return if the application is ready for operations
     */
    public static boolean isOperating() {
        return state == ApplicationState.OPERATING;
    }

    /**
     * Checks if the application is shutting down. If this is the case, no changes or new operatinons should be accepted.
     * @return if the application is closing
     */
    public static boolean isClosing() {
        return state == ApplicationState.CLOSING;
    }

}
