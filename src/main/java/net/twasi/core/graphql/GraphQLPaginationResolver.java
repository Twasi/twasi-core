package net.twasi.core.graphql;

import java.util.ArrayList;
import java.util.List;

public class GraphQLPaginationResolver {

    private static List<String> paginationTypes = new ArrayList<>();

    public static String resolve(String definitions) {
        try {
            String[] parts = definitions.split("pageable type ");
            for (int i = 1; i < parts.length; i++) {
                paginationTypes.add(parts[i].split("\\s+|\\{")[0]);
            }
            return definitions.replace("pageable type ", "type ");
        } catch (Exception e) {
            e.printStackTrace();
            return definitions;
        }
    }

    public static List<String> getPaginationTypes() {
        return new ArrayList<>(paginationTypes);
    }


    public static String getPaginationTypeDefinition() {
        /*
        return "union Pageable = " + String.join(" | ", paginationTypes);
         */
        StringBuilder definitions = new StringBuilder();
        paginationTypes.forEach(type -> definitions.append("\ntype %TYPE%Pageable { pages: Long, page: Long, total: Long, itemsPerPage: Long, content: [%TYPE%] } \n".replace("%TYPE%", type)));
        return definitions.toString();
    }

}
