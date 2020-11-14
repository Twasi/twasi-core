package net.twasi.core.database.models;

import org.mongodb.morphia.annotations.Entity;

import java.time.Instant;

@Entity("eventMessages")
public class EventMessage {
    private String message;
    private EventMessageType type;
    String createdAt = Instant.now().toString();

    public EventMessage(String message, EventMessageType type) {
        this.message = message;
        this.type = type;
    }

    public EventMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventMessageType getType() {
        return type;
    }

    public void setType(EventMessageType type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
