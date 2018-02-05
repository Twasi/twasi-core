package net.twasi.core.webinterface.controller.bot;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

public class BotInfoController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }

        User user = getUser(t);

        if (!InstanceManagerService.getService().hasRegisteredInstance(user)) {
            Commons.writeDTO(t, new BotInfoControllerDTO(false), 200);
            return;
        }

        TwasiInterface inf = InstanceManagerService.getService().getByUser(user);

        BotInfoControllerDTO dto = new BotInfoControllerDTO(true);

        Commons.writeDTO(t, dto, 200);
    }
}

class BotInfoControllerDTO extends ApiDTO {
    boolean isRunning;

    BotInfoControllerDTO(Boolean isRunning) {
        super(true);
        this.isRunning = isRunning;
    }
}
