package net.twasi.core.webinterface.controller.auth;

import net.twasi.core.interfaces.twitch.webapi.TwitchAPI;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

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
            String state;

            if (req.getParameter("environment") != null) {
                state = req.getParameter("environment");
            } else {
                state = "https://panel.twasi.net";
            }
            res.sendRedirect(ServiceRegistry.get(TwitchAPI.class).getAuthURL() + "&state=" + state);
        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect to twitch", e);
        }
    }
}
