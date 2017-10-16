package net.twasi.core.application;

/**
 * Represents the state of the application.
 *
 * Possible states:
 *
 * Starting - Application is starting up. Do not accept new operations until it's fully started
 * and all services are available.
 *
 * Operating - Application is ready to perform operations. This should be active if the application
 * is running.
 *
 * Closing - Application is shutting down. Some services may not be available anymore. Do not
 * accept new operations.
 */
public enum ApplicationState {

    STARTING,
    OPERATING,
    CLOSING

}
