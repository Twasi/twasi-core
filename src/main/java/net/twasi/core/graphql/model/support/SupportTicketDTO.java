package net.twasi.core.graphql.model.support;

import net.twasi.core.database.models.support.SupportTicket;

import java.util.List;
import java.util.stream.Collectors;

public class SupportTicketDTO {

    private SupportTicket ticket;

    public SupportTicketDTO(SupportTicket supportTicket) {
        this.ticket = supportTicket;
    }

    public SupportTicketUserDTO getOwner() {
        return new SupportTicketUserDTO(ticket.getOwner());
    }

    public List<SupportMessageDTO> getMessages() {
        return ticket.getEntries().stream()
                .map(SupportMessageDTO::new)
                .collect(Collectors.toList());
    }

    public String getState() {
        if (ticket.isOpen() && ticket.getEntries().size() == 1) {
            return "OPEN";
        } else if (ticket.isOpen()) {
            return "PROGRESS";
        } else {
            return "CLOSED";
        }
    }

    public String getTopic() {
        return ticket.getTopic();
    }

    public String getId() {
        return ticket.getId().toString();
    }

    public long getCreatedAt() {
        return ticket.getCreatedAt().getTime();
    }

    public long getClosedAt() {
        if (ticket.getClosedAt() == null) {
            return 0;
        }
        return ticket.getClosedAt().getTime();
    }

    public String getCategory() {
        return ticket.getCategory().toString();
    }
}
