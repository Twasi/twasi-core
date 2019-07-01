package net.twasi.core.graphql.model.support;

import net.twasi.core.database.models.support.SupportTicketMessage;

public class SupportMessageDTO {

    private SupportTicketMessage message;

    public SupportMessageDTO(SupportTicketMessage message) {
        this.message = message;
    }

    public SupportTicketUserDTO getSender() {
        return new SupportTicketUserDTO(message.getSender());
    }

    public String getMessage() {
        return message.getMessage();
    }

    public double getCreatedAt() {
        return message.getCreatedAt().getTime();
    }

    public boolean isStaff() {
        return message.isStaff();
    }

}
