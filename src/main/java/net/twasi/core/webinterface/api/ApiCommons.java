package net.twasi.core.webinterface.api;

import com.sun.net.httpserver.HttpExchange;
import org.bson.Document;

public class ApiCommons {

    public static Document getUnallowedMethod() {
        Document doc = new Document("success", false);
        doc.append("message", "Unknown or unhandled method");
        return doc;
    };

    public static void setReponseHeaders(HttpExchange t) {
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Content-Type", "application/json");
    }

}
