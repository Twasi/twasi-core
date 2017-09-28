package net.twasi.core.webinterface.api;

import org.bson.Document;

import com.sun.net.httpserver.HttpExchange;

public interface ApiEndpoint {

    Document getResponse(HttpExchange exchange);

}
