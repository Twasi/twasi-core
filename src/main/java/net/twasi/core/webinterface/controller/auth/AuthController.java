package net.twasi.core.webinterface.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.services.TwitchAPIService;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class AuthController extends RequestHandler {

    /**
     * Endpoint /auth
     * Redirect the user to the Twitch Oauth Interface. Callback is /auth/callback.
     *
     * @param t Exchange object
     */
    @Override
    public void handleGet(HttpExchange t) {
        Commons.writeRedirect(t, TwitchAPIService.getService().getAuthURL());
    }
}
