package net.twasi.core.webinterface.controller.auth;

import net.twasi.core.database.models.AccessToken;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.JWTService;
import net.twasi.core.services.providers.TwitchAPIService;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public class AuthCallbackController extends RequestHandler {

    @Override
    public void handleGet(Request req, HttpServletResponse res) {
        String code = req.getParameter("code");
        AccessToken accessToken = TwitchAPIService.getService().getToken(code);

        TwitchAccount account = TwitchAPIService.getService().getTwitchAccountByToken(accessToken);

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchAccountOrCreate(account);
        String token = ServiceRegistry.get(JWTService.class).getManager().createNewToken(user);

        if (!ServiceRegistry.get(InstanceManagerService.class).hasRegisteredInstance(user)) {
            ServiceRegistry.get(InstanceManagerService.class).registerInterface(new TwitchInterface(new Streamer(user)));
        }

        try {
            String redirectTo;

            // Decide where to redirect
            String state = req.getParameter("state");

            if (state == null ||
                    !(state.equalsIgnoreCase("https://panel.twasi.net") ||
                    state.equalsIgnoreCase("https://panel-beta.twasi.net") ||
                    state.equalsIgnoreCase("http://localhost:3000"))) {
                redirectTo = "https://panel.twasi.net";
            } else {
                redirectTo = state;
            }

            res.sendRedirect(redirectTo + "?jwt=" + token);
            //Commons.writeString(res, redirectTo + "?jwt=" + token, 200);
        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect back to frontend", e);
        }
    }
}
