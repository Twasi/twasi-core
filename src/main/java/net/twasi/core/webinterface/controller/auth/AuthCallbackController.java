package net.twasi.core.webinterface.controller.auth;

import net.twasi.core.database.models.AccessToken;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.interfaces.twitch.webapi.TwitchAPI;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.JWTService;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public class AuthCallbackController extends RequestHandler {

    @Override
    public void handleGet(Request req, HttpServletResponse res) {
        String code = req.getParameter("code");
        AccessToken accessToken = ServiceRegistry.get(TwitchAPI.class).getToken(code);

        TwitchAccount account = ServiceRegistry.get(TwitchAPI.class).getTwitchAccountByToken(accessToken);

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchAccountOrCreate(account);
        String token = ServiceRegistry.get(JWTService.class).getManager().createNewToken(user);

        if (!ServiceRegistry.get(InstanceManagerService.class).hasRegisteredInstance(user)) {
            ServiceRegistry.get(InstanceManagerService.class).registerInterface(new TwitchInterface(new Streamer(user)));
        }

        try {
            String redirectTo;

            // Decide where to redirect
            String state = req.getParameter("state");

            if (state == null || !(
                    state.equalsIgnoreCase("https://twasi.net") ||
                            state.equalsIgnoreCase("https://panel-beta.twasi.net") ||
                            state.equalsIgnoreCase("http://localhost:3000") ||
                            state.equalsIgnoreCase("https://dev.twasi.net/auth")
            )) {
                redirectTo = "https://panel.twasi.net";
            } else {
                redirectTo = state;
            }

            res.sendRedirect(redirectTo + "?jwt=" + token);

        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect back to frontend", e);
        }
    }
}
