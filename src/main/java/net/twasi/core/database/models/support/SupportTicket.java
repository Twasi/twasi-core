package net.twasi.core.database.models.support;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Reference;
import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity("supportTickets")
public class SupportTicket extends BaseEntity {

    @Reference
    private User owner;

    private List<SupportTicketMessage> entries;

    private boolean isOpen;

    private String topic;

    private SupportTicketType category;

    private Date createdAt;

    private Date closedAt;

    public SupportTicket() {
    }

    public SupportTicket(User owner, String topic, SupportTicketType category) {
        this.owner = owner;
        this.isOpen = true;
        this.entries = new ArrayList<>();
        this.topic = topic;
        this.createdAt = new Date();
        this.closedAt = null;
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public List<SupportTicketMessage> getEntries() {
        return entries;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public String getTopic() {
        return topic;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Date getCreatedAt() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        return createdAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public SupportTicketType getCategory() {
        if (category == null) {
            category = SupportTicketType.OTHER;
        }
        return category;
    }
}
