package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;

public class NotFoundController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        Commons.writeString(t, "404 - Not found :c", 404);
    }

    @Override
    public void handlePost(HttpExchange t) {
        Commons.writeString(t, "404 - Not found :c", 404);
    }

    @Override
    public void handlePut(HttpExchange t) {
        Commons.writeString(t, "404 - Not found :c", 404);
    }
}
