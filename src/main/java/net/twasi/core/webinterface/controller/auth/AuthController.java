package net.twasi.core.webinterface.controller.auth;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.TwitchAPIService;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthController extends RequestHandler {

    /**
     * Endpoint /auth
     * Redirect the user to the Twitch Oauth Interface. Callback is /auth/callback.
     *
     * @param req Request object
     * @param res Reponse object
     */
    @Override
    public void handleGet(Request req, HttpServletResponse res) {
        try {
            res.sendRedirect(TwitchAPIService.getService().getAuthURL());
        } catch (IOException e) {
            TwasiLogger.log.error("Could not redirect to twitch", e);
        }
    }
}
