package net.twasi.core.webinterface.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.config.Config;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class AuthController extends RequestHandler {

    /**
     * Endpoint /auth
     * Redirect the user to the Twitch Oauth Interface. Callback is /auth/callback.
     * @param t Exchange object
     */
    @Override
    public void handleGet(HttpExchange t) {
        Commons.writeRedirect(t, "https://api.twitch.tv/kraken/oauth2/authorize" +
                "?client_id=" + Config.catalog.twitch.clientId +
                "&redirect_uri=" + Config.catalog.twitch.redirectUri +
                "&response_type=code" +
                "&scope=channel_editor");
    }
}
