package net.twasi.core.graphql.model;

import net.twasi.core.database.models.EventMessageType;

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
}
