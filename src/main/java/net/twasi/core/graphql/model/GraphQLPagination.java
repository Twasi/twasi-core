package net.twasi.core.graphql.model;

import java.util.List;

public class GraphQLPagination<T> {

    private long total;
    private long itemsPerPage;
    private long page;
    private List<T> content;

    public GraphQLPagination(long total, long itemsPerPage, long page, List<T> content) {
        this.total = total;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
        this.content = content;
    }

    public final long getPages() {
        return total / itemsPerPage + ((total % itemsPerPage) == 0 ? 0 : 1);
    }

    public final long getPage() {
        return page;
    }

    public final long getTotal() {
        return total;
    }

    public final long getItemsPerPage() {
        return itemsPerPage;
    }

    public final List<T> getContent() {
        return content;
    }

}
