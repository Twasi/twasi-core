package net.twasi.core.webinterface.api;

import com.sun.net.httpserver.HttpExchange;
import org.bson.Document;

public class ApiCommons {

    public static Document UnallowedMethod = new Document() {{
        append("success", false);
        append("message", "Unknown or unhandled Method");
    }};

    public static void setReponseHeaders(HttpExchange t) {
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Content-Type", "application/json");
    }

}
