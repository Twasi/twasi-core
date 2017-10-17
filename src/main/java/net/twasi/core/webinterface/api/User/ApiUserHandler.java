package net.twasi.core.webinterface.api.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.twasi.core.webinterface.api.ApiCommons;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.io.IOException;
import java.io.OutputStream;

public class ApiUserHandler implements HttpHandler {

    UserGet get = new UserGet();
    UserPost post = new UserPost();

    public void handle(HttpExchange t) throws IOException {

        Document response;
        OutputStream os = t.getResponseBody();

        switch(t.getRequestMethod().toLowerCase()) {
            case "get":
                response = get.getResponse(t);
                break;
            default:
                response = ApiCommons.getUnallowedMethod();
        }

        String resp = response.toJson(new JsonWriterSettings(JsonMode.STRICT, true));

        ApiCommons.setReponseHeaders(t);

        t.sendResponseHeaders(200, resp.length());
        os.write(resp.getBytes());
        os.close();
    }
}
