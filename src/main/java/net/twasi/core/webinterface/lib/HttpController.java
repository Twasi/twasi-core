package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public interface HttpController {

    public void handleGet(Request t, HttpServletResponse r);

    public void handlePost(Request t, HttpServletResponse r);

    public void handlePut(Request t, HttpServletResponse r);

}
