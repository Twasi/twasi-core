package net.twasi.core.webinterface.controller.auth;

import net.twasi.core.config.Config;
import net.twasi.core.database.models.AccessToken;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.InstanceManagerService;
import net.twasi.core.services.providers.JWTService;
import net.twasi.core.services.providers.TwitchAPIService;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletResponse;

public class AuthCallbackController extends RequestHandler {

    @Override
    public void handleGet(Request req, HttpServletResponse res) {
        String code = req.getParameter("code");
        AccessToken accessToken = TwitchAPIService.getService().getToken(code);

        TwitchAccount account = TwitchAPIService.getService().getTwitchAccountByToken(accessToken);

        User user = UserStore.getOrCreate(account);
        String token = JWTService.getService().createNewToken(user);

        if (!InstanceManagerService.getService().hasRegisteredInstance(user)) {
            InstanceManagerService.getService().registerInterface(new TwitchInterface(new Streamer(user)));
        }

        try {
            res.sendRedirect(ServiceRegistry.get(ConfigService.class).getCatalog().webinterface.frontend + "?jwt=" + token);
            //Commons.writeString(res, Config.getCatalog().webinterface.frontend + "?jwt=" + token, 200);
        } catch (Exception e) {
            TwasiLogger.log.error("Could not redirect back to frontend", e);
        }
    }
}
