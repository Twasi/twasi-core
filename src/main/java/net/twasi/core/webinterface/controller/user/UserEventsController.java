package net.twasi.core.webinterface.controller.user;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.EventMessage;
import net.twasi.core.database.models.User;
import net.twasi.core.services.InstanceManagerService;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.InfoDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.util.List;

public class UserEventsController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }
        User user = getUser(t);

        List<EventMessage> messages = user.getEvents();

        UserEventDTO dto = new UserEventDTO(messages);

        Commons.writeDTO(t, dto, 200);
    }

    class UserEventDTO extends ApiDTO {
        List<EventMessage> messages;

        private UserEventDTO(List<EventMessage> messages) {
            super(true);
            this.messages = messages;
        }
    }
}
