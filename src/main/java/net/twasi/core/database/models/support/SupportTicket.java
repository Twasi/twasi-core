package net.twasi.core.database.models.support;

import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.database.models.User;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity("supportTickets")
public class SupportTicket extends BaseEntity {

    public SupportTicket() {
    }

    public SupportTicket(User owner, String topic) {
        this.owner = owner;
        this.isOpen = true;
        this.entries = new ArrayList<>();
        this.topic = topic;
        this.createdAt = new Date();
        this.closedAt = null;
    }

    @Reference
    private User owner;

    private List<SupportTicketMessage> entries;

    private boolean isOpen;

    private String topic;

    private Date createdAt;

    private Date closedAt;

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
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }
}
