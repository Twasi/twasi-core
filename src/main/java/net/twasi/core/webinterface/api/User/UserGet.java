package net.twasi.core.webinterface.api.User;

import com.mongodb.DBObject;
import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.webinterface.api.ApiEndpoint;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class UserGet implements ApiEndpoint {
    @Override
    public Document getResponse(HttpExchange exchange) {

        List users = Database.getStore().createQuery(User.class).asList();

        List<DBObject> mapped = (List<DBObject>) users.stream().map(e -> Database.getMorphia().toDBObject(e)).collect(Collectors.toList());

        Document doc = new Document("status", true);
        doc.append("users", mapped);
        doc.append("num", 0);
        return doc;
    }
}
