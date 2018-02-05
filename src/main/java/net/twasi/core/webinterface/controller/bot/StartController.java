package net.twasi.core.webinterface.controller.bot;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.User;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.ErrorDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class StartController extends RequestHandler {

    @Override
    public void handlePost(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }

        User user = getUser(t);

        if (InstanceManagerService.getService().hasRegisteredInstance(user)) {
            Commons.writeDTO(t, new ErrorDTO(false, "Bot already running."), 500);
            return;
        }

        InstanceManagerService.getService().start(user);

        Commons.writeDTO(t, new StartControllerSuccessDTO(InstanceManagerService.getService().hasRegisteredInstance(user)), 200);
    }
}

class StartControllerSuccessDTO extends ApiDTO {
    boolean isRunning;

    public StartControllerSuccessDTO(Boolean isRunning) {
        super(true);
        this.isRunning = isRunning;
    }
}
