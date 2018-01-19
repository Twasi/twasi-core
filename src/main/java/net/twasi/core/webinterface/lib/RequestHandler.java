package net.twasi.core.webinterface.lib;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.prometheus.client.Counter;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.JWTService;
import net.twasi.core.webinterface.dto.error.NotFoundDTO;

public abstract class RequestHandler implements HttpHandler, HttpController {
    private static final Counter requests = Counter.build()
            .name("requests_total").help("Total requests.").register();

    @Override
    public void handle(HttpExchange httpExchange) {
        requests.inc();

        if (!httpExchange.getHttpContext().getPath().equalsIgnoreCase(httpExchange.getRequestURI().getPath())) {
            Commons.writeDTO(httpExchange, new NotFoundDTO(), 404);
            return;
        }

        try {
            switch (httpExchange.getRequestMethod().toLowerCase()) {
                case "get":
                    handleGet(httpExchange);
                    break;
                case "post":
                    handlePost(httpExchange);
                    break;
                case "put":
                    handlePut(httpExchange);
                    break;
                case "options":
                    httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Authorization");
                    Commons.handleOptions(httpExchange);
                    break;
                default:
                    Commons.handleUnallowedMethod(httpExchange);
                    break;
            }
        } catch (Throwable e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void handleGet(HttpExchange t) {
        Commons.handleUnallowedMethod(t);
    }

    @Override
    public void handlePost(HttpExchange t) {
        Commons.handleUnallowedMethod(t);
    }

    @Override
    public void handlePut(HttpExchange t) { Commons.handleUnallowedMethod(t); }

    private String getToken(HttpExchange t) {
        String authorizationHeader = t.getRequestHeaders().getFirst("Authorization");
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

    protected boolean isAuthenticated(HttpExchange t) {
        String jwt = getToken(t);

        return jwt != null && JWTService.getService().isValidToken(jwt);

    }

    public User getUser(HttpExchange t) {
        return JWTService.getService().getUserFromToken(getToken(t));
    }
}
