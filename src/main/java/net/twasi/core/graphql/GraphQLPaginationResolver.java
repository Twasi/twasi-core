package net.twasi.core.graphql;

public class GraphQLPaginationResolver {

    public static String resolve(String definitions) {
        String[] parts = definitions.split("pageable type ");
        StringBuilder schema = new StringBuilder(definitions.replace("pageable type ", "type "));
        for (int i = 1; i < parts.length; i++) {
            schema.append(getPaginationTypeDefinition(parts[i].split("\\s+|\\{")[0]));
        }
        return schema.append("\n").toString();
    }

    public static String getPaginationTypeDefinition(String type) {
        return "\n\ntype %TYPE%Pageable { pages: Long, total: Long, itemsPerPage: Long, content(page: Int): [%TYPE%] }".replace("%TYPE%", type);
    }

}
