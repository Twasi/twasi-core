package net.twasi.core.webinterface.controller;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.User;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.services.JWTService;
import net.twasi.core.webinterface.dto.InfoDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class InfoController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }

        User user = getUser(t);
        boolean isInstanceRunning = InstanceManagerService.getService().hasRegisteredInstance(user);

        Commons.writeDTO(t, new InfoDTO(true, user.getTwitchAccount().getTwitchId(), user.getTwitchAccount().getUserName(), isInstanceRunning), 200);
    }
}
