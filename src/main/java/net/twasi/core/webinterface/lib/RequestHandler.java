package net.twasi.core.webinterface.lib;

import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.JWTService;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RequestHandler extends AbstractHandler {
    //private static final Counter requests = Counter.build()
    //        .name("requests_total").help("Total requests.").register();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //requests.inc();

        /* if (!httpExchange.getHttpContext().getPath().equalsIgnoreCase(httpExchange.getRequestURI().getPath())) {
            Commons.writeDTO(httpExchange, new NotFoundDTO(), 404);
            return;
        } */

        try {
            switch (baseRequest.getMethod().toLowerCase()) {
                case "get":
                    handleGet(baseRequest, response);
                    break;
                case "post":
                    handlePost(baseRequest, response);
                    break;
                case "put":
                    handlePut(baseRequest, response);
                    break;
                case "options":
                    response.setHeader("Access-Control-Allow-Headers", "Authorization");
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Access-Control-Allow-Methods", "*");
                    response.setStatus(200);
                    baseRequest.setHandled(true);
                    response.getWriter().println("OPTIONS OK");
                    break;
                default:
                    //Commons.handleUnallowedMethod(response);
                    response.setStatus(405);
                    baseRequest.setHandled(true);
                    break;
            }
        } catch (Throwable e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
        }

        // Commit the user
        User user = getUser(baseRequest);
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);
    }

    public void handleGet(Request t, HttpServletResponse r) throws IOException {
        r.setStatus(405);
        t.setHandled(true);
    }

    public void handlePost(Request t, HttpServletResponse r) throws IOException {
        r.setStatus(405);
        t.setHandled(true);
    }

    public void handlePut(Request t, HttpServletResponse r) throws IOException {
        r.setStatus(405);
        t.setHandled(true);
    }

    private String getToken(Request t) {
        String authorizationHeader = t.getHeader("Authorization");
        if (authorizationHeader == null) {
            return null;
        }

        String[] splitted = authorizationHeader.split(" ", 2);

        if (splitted.length != 2) {
            return null;
        }

        if (!splitted[0].equals("Bearer")) {
            return null;
        }

        return splitted[1];
    }

    public User getUser(Request t) {
        return ServiceRegistry.get(JWTService.class).getManager().getUserFromToken(getToken(t));
    }
}
