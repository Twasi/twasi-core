package net.twasi.core.graphql.model.support;

import net.twasi.core.graphql.model.GraphQLPagination;

import java.util.List;

public class SupportTicketPagination extends GraphQLPagination<SupportTicketDTO> {

    public SupportTicketPagination(long total, long page, List<SupportTicketDTO> content) {
        super(total, page, content);
    }

}
