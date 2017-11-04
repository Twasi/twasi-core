package net.twasi.core.webinterface.controller.user;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.webinterface.dto.UserPostDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.bson.Document;

import java.io.InputStreamReader;
import java.io.Reader;

public class UserController extends RequestHandler {

    public void handlePost(HttpExchange t) {
        Reader reader = new InputStreamReader(t.getRequestBody());
        UserPostDTO user = new Gson().fromJson(reader, UserPostDTO.class);

        if (!user.isValid()) {
            Commons.handleBadRequest(t);
        }

        Document doc = new Document();
        doc.append("success", true);

        Commons.writeDocument(t, doc, 200);
    }

    public void handleGet(HttpExchange t) {
        Commons.writeString(t, "Get called", 200);
    }
}
