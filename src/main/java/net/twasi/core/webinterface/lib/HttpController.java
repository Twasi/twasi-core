package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;

public interface HttpController {

    public void handleGet(HttpExchange t);

    public void handlePost(HttpExchange t);

}
