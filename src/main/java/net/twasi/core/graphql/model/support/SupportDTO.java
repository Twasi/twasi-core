package net.twasi.core.graphql.model.support;

import net.twasi.core.database.models.User;
import net.twasi.core.database.models.UserRank;
import net.twasi.core.database.models.support.SupportTicket;
import net.twasi.core.database.repositories.SupportTicketRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;

import java.util.List;
import java.util.stream.Collectors;

public class SupportDTO {

    private User user;
    private SupportTicketRepository repository;

    public SupportDTO(User user) {
        repository = ServiceRegistry.get(DataService.class).get(SupportTicketRepository.class);
        this.user = user;
    }

    public List<SupportTicketDTO> getMyTickets() {
        return repository.getByUser(user).stream()
                .map(SupportTicketDTO::new)
                .collect(Collectors.toList());
    }

    public List<SupportTicketDTO> getAdminTickets() {
        if (!user.getRank().equals(UserRank.TEAM)) {
            return null;
        }

        return repository.getAll().stream()
                .sorted((f1, f2) -> Boolean.compare(f1.isOpen(), f2.isOpen()))
                .map(SupportTicketDTO::new)
                .collect(Collectors.toList());
    }

    public SupportTicketDTO create(String topic, String message) {
        SupportTicket ticket = repository.create(user, topic, message);

        return new SupportTicketDTO(ticket);
    }

    public SupportTicketDTO reply(String id, String message, Boolean close, Boolean isAdminContext) {
        SupportTicket ticket = null;

        if (isAdminContext) {
            if (user.getRank() != UserRank.TEAM) {
                // Not permitted to post admin response.
                return null;
            }
        }

        if (!isAdminContext) {
            // Only search for personal tickets
            ticket = repository.getByUser(user).stream().filter(t -> t.getId().toString().equals(id)).findFirst().orElse(null);
        } else {
            // Search for all tickets, verified admin
            ticket = repository.getById(id);
        }

        // Ticket not found :(
        if (ticket == null) {
            return null;
        }

        repository.addReply(ticket, user, isAdminContext, message, close);

        ticket = repository.getById(ticket.getId());
        return new SupportTicketDTO(ticket);
    }

}
