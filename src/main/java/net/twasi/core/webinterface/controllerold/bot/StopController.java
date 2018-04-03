package net.twasi.core.webinterface.controllerold.bot;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.User;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.ErrorDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class StopController extends RequestHandler {

    @Override
    public void handlePost(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }

        User user = getUser(t);

        if (!InstanceManagerService.getService().hasRegisteredInstance(user)) {
            Commons.writeDTO(t, new ErrorDTO(false, "Bot not running."), 500);
            return;
        }

        InstanceManagerService.getService().stop(user);

        Commons.writeDTO(t, new StopControllerSuccessDTO(InstanceManagerService.getService().hasRegisteredInstance(user)), 200);
    }
}

class StopControllerSuccessDTO extends ApiDTO {
    boolean isRunning;

    public StopControllerSuccessDTO(Boolean isRunning) {
        super(true);
        this.isRunning = isRunning;
    }
}
