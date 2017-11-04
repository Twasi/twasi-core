package net.twasi.core.webinterface.api.User;

import com.google.gson.Gson;
import net.twasi.core.webinterface.api.ApiEndpoint;
import net.twasi.core.webinterface.dto.UserPostDTO;
import org.bson.Document;

import com.sun.net.httpserver.HttpExchange;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class UserPost implements ApiEndpoint {
    @Override
    public Document getResponse(HttpExchange exchange) {

        Reader reader = new InputStreamReader(exchange.getRequestBody());
        UserPostDTO user = new Gson().fromJson(reader, UserPostDTO.class);

        Document doc = new Document();
        doc.append("success", true);

        return doc;
    }
}
