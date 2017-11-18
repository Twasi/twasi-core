package net.twasi.core.webinterface.controller.user;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class UserRefreshController extends RequestHandler {

    @Override
    public void handlePost(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }

        User user = getUser(t);

        UserStore.updateUser(user);
        Commons.writeDTO(t, new ApiDTO(true), 200);
    }
}
