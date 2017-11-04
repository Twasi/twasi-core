package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

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
}
