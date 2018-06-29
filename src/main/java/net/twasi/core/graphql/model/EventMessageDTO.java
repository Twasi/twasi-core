package net.twasi.core.graphql.model;

import net.twasi.core.database.models.EventMessage;
import net.twasi.core.database.models.EventMessageType;

import java.util.List;
import java.util.stream.Collectors;

public class EventMessageDTO {
    private EventMessage msg;

    public EventMessageDTO(EventMessage msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg.getMessage();
    }

    public EventMessageType getMessageType() {
        return msg.getType();
    }

    public String getCreatedAt() {
        return msg.getCreatedAt();
    }

    public static List<EventMessageDTO> fromEvents(List<EventMessage> events) {
        return events
                .stream()
                .map(EventMessageDTO::new)
                .collect(Collectors.toList());
    }
}
