package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;

public interface Controller {

    public String getResponse(HttpExchange exchange);

}
