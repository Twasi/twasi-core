package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public class NotFoundController extends RequestHandler {

    @Override
    public void handleGet(Request req, HttpServletResponse res) {
        Commons.writeString(res, "404 - Not found :c", 404);
    }

    @Override
    public void handlePost(Request req, HttpServletResponse res) {
        Commons.writeString(res, "404 - Not found :c", 404);
    }

    @Override
    public void handlePut(Request req, HttpServletResponse res) {
        Commons.writeString(res, "404 - Not found :c", 404);
    }
}
