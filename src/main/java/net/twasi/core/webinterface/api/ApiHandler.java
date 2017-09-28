package net.twasi.core.webinterface.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class ApiHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {
        URI uri = t.getRequestURI();
        System.out.println("api call for: "+ uri.getPath());

        // Object does not exist or is not a file: reject with 404 error.
        String response = "{\"status\":[{\"component\":\"Twasi-Core\",\"description\":\"Twasi Main program\",\"status\":false}]}\n";
        t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().set("Content-Type", "application/json");
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}