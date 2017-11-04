package net.twasi.core.webinterface.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class AuthCallbackController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        Commons.writeString(t, "Authentication Callback", 200);
    }
}
