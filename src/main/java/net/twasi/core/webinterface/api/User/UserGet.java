package net.twasi.core.webinterface.api.User;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.webinterface.api.ApiEndpoint;
import org.bson.Document;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.stream.Collectors;

public class UserGet implements ApiEndpoint {
    @Override
    public Document getResponse(HttpExchange exchange) {

        List users = Database.getStore().createQuery(User.class).asList();
        //System.out.println(users);
        Gson gson = new Gson();

        List<DBObject> mapped = (List<DBObject>) users.stream().map(e -> Database.getMorphia().toDBObject(e)).collect(Collectors.toList());

        return new Document("status", true){{
            append("users", mapped);
            append("num", 0);
        }};
    }
}
