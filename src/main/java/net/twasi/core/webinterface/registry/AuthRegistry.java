package net.twasi.core.webinterface.registry;

import net.twasi.core.webinterface.controller.auth.AuthCallbackController;
import net.twasi.core.webinterface.controller.auth.AuthController;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class AuthRegistry {

    public static HandlerCollection register() {
        HandlerCollection collection = new HandlerCollection();

        // Callback
        ContextHandler authCallbackHandler = new ContextHandler();
        authCallbackHandler.setContextPath("/authcallback");
        authCallbackHandler.setHandler(new AuthCallbackController());
        collection.addHandler(authCallbackHandler);

        // Authenticate
        ContextHandler authHandler = new ContextHandler();
        authHandler.setContextPath("/auth");
        authHandler.setHandler(new AuthController());
        collection.addHandler(authHandler);

        return collection;
    }

}
