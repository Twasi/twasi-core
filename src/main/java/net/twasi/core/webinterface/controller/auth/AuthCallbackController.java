package net.twasi.core.webinterface.controller.auth;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.services.JWTService;
import net.twasi.core.services.TwitchAPIService;
import net.twasi.core.webinterface.dto.auth.AccessTokenDTO;
import net.twasi.core.webinterface.dto.auth.TokenInfoDTO;
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

        // Commons.writeString(t, "Your Access Token: " + accessToken.getAccessToken(), 200);
    }
}
