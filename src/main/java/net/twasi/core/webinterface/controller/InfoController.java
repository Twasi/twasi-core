package net.twasi.core.webinterface.controller;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.webinterface.lib.RequestHandler;

public class InfoController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (isAuthenticated(t)) {
            System.out.println("Authenticated.");
        } else {
            System.out.println("Please sign in.");
        }
    }
}
