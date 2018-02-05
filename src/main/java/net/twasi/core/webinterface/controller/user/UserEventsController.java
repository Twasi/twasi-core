package net.twasi.core.webinterface.controller.user;

import com.sun.net.httpserver.HttpExchange;
import net.twasi.core.database.models.EventMessage;
import net.twasi.core.database.models.User;
import net.twasi.core.webinterface.dto.ApiDTO;
import net.twasi.core.webinterface.dto.error.UnauthorizedDTO;
import net.twasi.core.webinterface.lib.Commons;
import net.twasi.core.webinterface.lib.RequestHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserEventsController extends RequestHandler {

    @Override
    public void handleGet(HttpExchange t) {
        if (!isAuthenticated(t)) {
            Commons.writeDTO(t, new UnauthorizedDTO(), 401);
            return;
        }
        User user = getUser(t);

        List<EventMessage> messages = user.getEvents();
        Collections.reverse(messages);
        if (messages.size() >= 10) {
            messages = messages.subList(0, 10);
        }

        UserEventDTO dto = new UserEventDTO(messages);

        Commons.writeDTO(t, dto, 200);
    }

    class UserEventDTO extends ApiDTO {
        List<Object> messages;

        private UserEventDTO(List<EventMessage> messages) {
            super(true);
            this.messages = messages.stream().map(msg -> new UserSingleEventDTO(msg.getMessage(), msg.getCreatedAt(), msg.getType().toString())).collect(Collectors.toList());
        }

        class UserSingleEventDTO {
            String message;
            String createdAt;
            String type;

            public UserSingleEventDTO(String message, String createdAt, String type) {
                this.message = message;
                this.createdAt = createdAt;
                this.type = type;
            }
        }
    }
}
