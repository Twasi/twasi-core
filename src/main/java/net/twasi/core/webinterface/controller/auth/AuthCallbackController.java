package net.twasi.core.webinterface.controller.auth;

import io.prometheus.client.Counter;
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
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.twitchapi.TwitchAPI;
import net.twasi.twitchapi.helix.users.response.UserDTO;
import net.twasi.twitchapi.id.oauth2.response.TokenDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthCallbackController extends HttpServlet {
    private static final Counter authCallbacks = Counter.build()
            .name("auth_callbacks_total").help("Total callbacks from Twitch OAuth.").register();
    private static final Counter logins = Counter.build()
            .name("logins_total").help("Total users logging in (using Twitch OAuth).").register();
    private static final Counter failedLogins = Counter.build()
            .name("logins_failed_total").help("Total users failed logging in (using Twitch OAuth).").register();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        authCallbacks.inc();

        String code = req.getParameter("code");

        if (code == null) {
            res.sendError(400, "Code not found.");
            return;
        }

        TokenDTO token = TwitchAPI.authentication().getToken(code);
        if (token == null) {
            res.setStatus(500);
            res.sendError(500, "Failed to authenticate, code validation via twitch failed.");
            TwasiLogger.log.debug("Failed to log in, code verification via twitch failed.");
            failedLogins.inc();
            return;
        }

        AccessToken accessToken = new AccessToken(token);

        UserDTO userData = TwitchAPI.helix().users().withAuth(accessToken.toAuthContext(null)).getCurrentUser();

        if (userData == null) {
            res.setStatus(500);
            res.sendError(500, "Failed to authenticate, account retrieval via twitch failed.");
            TwasiLogger.log.debug("Failed to log in, cannot retrieve account via twitch.");
            failedLogins.inc();
            return;
        }

        TwitchAccount account = TwitchAccount.fromUser(userData, accessToken);

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchAccountOrCreate(account);
        String jwtToken = ServiceRegistry.get(JWTService.class).getManager().createNewToken(user);

        if (!ServiceRegistry.get(InstanceManagerService.class).hasRegisteredInstance(user)) {
            ServiceRegistry.get(InstanceManagerService.class).start(user);
        }

        try {
            String redirectTo;

            // Decide where to redirect
            String state = req.getParameter("state");

            if (state == null || !(ServiceRegistry.get(ConfigService.class).getCatalog().auth.endpoints.contains(state))) {
                redirectTo = "https://panel.twasi.net";
            } else {
                redirectTo = state;
            }

            logins.inc();
            res.sendRedirect(redirectTo + "?jwt=" + jwtToken);

        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect back to frontend", e);
        }
    }
}
