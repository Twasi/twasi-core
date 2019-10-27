package net.twasi.core.graphql.model.customthemes;

import net.twasi.core.graphql.model.GraphQLPagination;

import java.util.List;

// TODO remove
public class CustomThemePagination extends GraphQLPagination<StoreCustomThemeDTO> {

    public CustomThemePagination(long total, long page, List<StoreCustomThemeDTO> content) {
        super(total, page, content);
    }

}
