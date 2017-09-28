package net.twasi.core.webinterface.api.User;

import net.twasi.core.webinterface.api.ApiEndpoint;
import org.bson.Document;

import com.sun.net.httpserver.HttpExchange;

public class UserPost implements ApiEndpoint {
    @Override
    public Document getResponse(HttpExchange exchange) {

        Document doc = new Document();
        doc.append("success", true);

        return doc;
    }
}
