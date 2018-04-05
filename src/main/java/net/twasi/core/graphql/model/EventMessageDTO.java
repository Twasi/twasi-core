package net.twasi.core.graphql.model;

import net.twasi.core.database.models.EventMessage;
import net.twasi.core.database.models.EventMessageType;

import java.util.List;
import java.util.stream.Collectors;

public class EventMessageDTO {
    private final String message;
    private final EventMessageType messageType;
    private final String createdAt;

    public EventMessageDTO(String message, EventMessageType messageType, String createdAt) {
        this.message = message;
        this.messageType = messageType;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public EventMessageType getMessageType() {
        return messageType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static List<EventMessageDTO> fromEvents(List<EventMessage> events) {
        return events
                .stream()
                .map(event ->
                        new EventMessageDTO(event.getMessage(), event.getType(), event.getCreatedAt())
                ).collect(Collectors.toList());
    }
}
