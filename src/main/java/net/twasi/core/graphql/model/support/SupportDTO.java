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
                .map(ticket -> new SupportTicketDTO(user, ticket))
                .collect(Collectors.toList());
    }

    public List<SupportTicketDTO> getAdminTickets() {
        if (!user.getRank().equals(UserRank.TEAM)) {
            return null;
        }

        return repository.getAll().stream()
                .sorted((f1, f2) -> Boolean.compare(f1.isOpen(), f2.isOpen()))
                .map(ticket -> new SupportTicketDTO(user, ticket, true))
                .collect(Collectors.toList());
    }

    public SupportTicketDTO create(String topic, String message) {
        SupportTicket ticket = repository.create(user, topic, message);

        return new SupportTicketDTO(user, ticket);
    }

}
