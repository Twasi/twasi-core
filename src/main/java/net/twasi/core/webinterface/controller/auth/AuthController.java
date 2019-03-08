package net.twasi.core.webinterface.controller.auth;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.twitchapi.TwitchAPI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String state;

            if (req.getParameter("environment") != null) {
                state = req.getParameter("environment");
            } else {
                state = "https://panel.twasi.net";
            }
            resp.sendRedirect(TwitchAPI.authentication().getAuthURL(ServiceRegistry.get(ConfigService.class).getCatalog().twitch.scopes, state));
        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect to twitch", e);
            throw e;
        }
    }
}
