package net.twasi.core.webinterface.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.services.TwitchAPIService;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class AuthCallbackController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        String code = Commons.parseQueryParams(t).get("code");
        String accessToken = TwitchAPIService.getService().getAccessToken(code);

        Commons.writeString(t, "Your Access Token: " + accessToken, 200);
    }
}
