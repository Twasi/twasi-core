package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.twasi.core.database.models.User;
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

    private String getToken(HttpExchange t) {
        String authorizationHeader = t.getRequestHeaders().getFirst("Authorization");
        if (authorizationHeader == null) {
            return null;
        }

        String[] splitted = authorizationHeader.split(" ", 2);

        if (splitted.length != 2) {
            return null;
        }

        if (!splitted[0].equals("Bearer")) {
            return null;
        }

        return splitted[1];
    }

    protected boolean isAuthenticated(HttpExchange t) {
        String jwt = getToken(t);

        return jwt != null && JWTService.getService().isValidToken(jwt);

    }

    public User getUser(HttpExchange t) {
        return JWTService.getService().getUserFromToken(getToken(t));
    }
}
