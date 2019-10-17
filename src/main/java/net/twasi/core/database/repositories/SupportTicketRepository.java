package net.twasi.core.database.repositories;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.support.SupportTicket;
import net.twasi.core.database.models.support.SupportTicketMessage;
import net.twasi.core.database.models.User;
import net.twasi.core.database.models.support.SupportTicketType;
import org.mongodb.morphia.query.FindOptions;

import java.util.Date;
import java.util.List;

public class SupportTicketRepository extends Repository<SupportTicket> {

    public SupportTicket create(User owner, String topic, String message, SupportTicketType category) {
        SupportTicket st = new SupportTicket(owner, topic, category);

        SupportTicketMessage stm = new SupportTicketMessage(owner, message, false);

        st.getEntries().add(stm);

        store.save(st);

        return st;
    }

    public List<SupportTicket> getByUser(User owner, int max, int page) {
        return query().field("owner").equal(owner).asList(new FindOptions().skip((page - 1) * max).limit(max));
    }

    public List<SupportTicket> getByUser(User owner) {
        return query().field("owner").equal(owner).asList();
    }

    public List<SupportTicket> getAll(int max, int page) {
        return query().asList(new FindOptions().skip((page - 1) * max).limit(max));
    }

    public SupportTicketMessage addReply(SupportTicket ticket, User user, boolean isAdminContext, String message, boolean close) {
        SupportTicketMessage stm = new SupportTicketMessage(user, message, isAdminContext);

        ticket.getEntries().add(stm);

        if (close) {
            ticket.setOpen(false);
            ticket.setClosedAt(new Date());
        }

        store.save(ticket);

        return stm;
    }

    public long countByUser(User owner) {
        return query().field("owner").equal(owner).count();
    }
}
