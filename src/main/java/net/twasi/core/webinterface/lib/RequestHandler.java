package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.twasi.core.services.JWTService;

import java.io.IOException;
import java.util.List;

public abstract class RequestHandler implements HttpHandler, HttpController {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        switch(httpExchange.getRequestMethod().toLowerCase()) {
            case "get":
                handleGet(httpExchange);
                break;
            case "post":
                handlePost(httpExchange);
                break;
            default:
                Commons.handleUnallowedMethod(httpExchange);
                break;
        }
    }

    @Override
    public void handleGet(HttpExchange t) {
        Commons.handleUnallowedMethod(t);
    }

    @Override
    public void handlePost(HttpExchange t) {
        Commons.handleUnallowedMethod(t);
    }

    public boolean isAuthenticated(HttpExchange t) {
        String authorizationHeader = t.getRequestHeaders().getFirst("Authorization");
        if (authorizationHeader == null) {
            return false;
        }

        String[] splitted = authorizationHeader.split(" ", 2);

        if (splitted.length != 2) {
            return false;
        }

        if (!splitted[0].equals("Bearer")) {
            return false;
        }

        return JWTService.getService().isValidToken(splitted[1]);
    }
}
