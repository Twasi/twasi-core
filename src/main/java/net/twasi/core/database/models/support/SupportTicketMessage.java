package net.twasi.core.database.models.support;

import jdk.nashorn.internal.ir.annotations.Reference;
import net.twasi.core.database.models.User;

import java.util.Date;

public class SupportTicketMessage {

    public SupportTicketMessage() {
    }

    public SupportTicketMessage(User sender, String message, boolean isStaff) {
        this.sender = sender;
        this.message = message;
        this.createdAt = new Date();
        this.isStaff = isStaff;
    }

    @Reference
    private User sender;

    private Date createdAt;

    private String message;

    private boolean isStaff;

    public User getSender() {
        return sender;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStaff() {
        return isStaff;
    }
}
