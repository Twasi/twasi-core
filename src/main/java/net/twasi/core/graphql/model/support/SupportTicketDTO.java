package net.twasi.core.graphql.model.support;

import net.twasi.core.database.models.User;
import net.twasi.core.database.models.support.SupportTicket;
import net.twasi.core.database.models.support.SupportTicketMessage;
import net.twasi.core.database.repositories.SupportTicketRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;

import java.util.List;
import java.util.stream.Collectors;

public class SupportTicketDTO {

    private SupportTicket ticket;

    private boolean isAdminContext;
    private User user;

    public SupportTicketDTO(User user, SupportTicket supportTicket) {
        this.ticket = supportTicket;
        this.isAdminContext = false;
    }

    public SupportTicketDTO(User user, SupportTicket supportTicket, boolean isAdminContext) {
        this(user, supportTicket);
        this.isAdminContext = isAdminContext;
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

    public SupportMessageDTO reply(String message, boolean close) {
        SupportTicketMessage stm = ServiceRegistry.get(DataService.class).get(SupportTicketRepository.class).addReply(ticket, user, isAdminContext, message, close);

        return new SupportMessageDTO(stm);
    }

}
