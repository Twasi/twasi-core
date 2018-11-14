package net.twasi.core.webinterface.controller.auth;

import io.prometheus.client.Counter;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        authCallbacks.inc();

        String code = req.getParameter("code");

        if (code == null) {
            res.sendError(400, "Code not found.");
            return;
        }

        AccessToken accessToken = ServiceRegistry.get(TwitchAPI.class).getToken(code);
        if (accessToken == null) {
            res.setStatus(500);
            res.sendError(500, "Failed to authenticate, code validation via twitch failed.");
            TwasiLogger.log.debug("Failed to log in, code verification via twitch failed.");
            failedLogins.inc();
            return;
        }

        TwitchAccount account = ServiceRegistry.get(TwitchAPI.class).getTwitchAccountByToken(accessToken);
        if (account == null) {
            res.setStatus(500);
            res.sendError(500, "Failed to authenticate, account retrieval via twitch failed.");
            TwasiLogger.log.debug("Failed to log in, cannot retrieve account via twitch.");
            failedLogins.inc();
            return;
        }

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchAccountOrCreate(account);
        String token = ServiceRegistry.get(JWTService.class).getManager().createNewToken(user);

        if (!ServiceRegistry.get(InstanceManagerService.class).hasRegisteredInstance(user)) {
            ServiceRegistry.get(InstanceManagerService.class).registerInterface(new TwitchInterface(new Streamer(user)));
            ServiceRegistry.get(InstanceManagerService.class).start(user);
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

            logins.inc();
            res.sendRedirect(redirectTo + "?jwt=" + token);

        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect back to frontend", e);
        }
    }
}
