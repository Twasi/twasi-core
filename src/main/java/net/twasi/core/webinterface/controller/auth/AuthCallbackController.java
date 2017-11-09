package net.twasi.core.webinterface.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.models.Streamer;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.services.JWTService;
import net.twasi.core.services.TwitchAPIService;
import net.twasi.core.webinterface.dto.auth.AccessTokenDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class AuthCallbackController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        String code = Commons.parseQueryParams(t).get("code");
        AccessTokenDTO accessToken = TwitchAPIService.getService().getToken(code);

        TwitchAccount account = TwitchAPIService.getService().getTwitchAccountByToken(accessToken);

        User user = UserStore.getOrCreate(account);
        String token = JWTService.getService().createNewToken(user);

        if (!InstanceManagerService.getService().hasRegisteredInstance(user)) {
            InstanceManagerService.getService().registerInterface(new TwitchInterface(new Streamer(user)));
        }

        Commons.writeRedirect(t, "/login?jwt="+token);
    }
}
