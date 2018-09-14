package net.twasi.core.graphql;

import net.twasi.core.database.models.User;
import net.twasi.core.graphql.model.*;

public class Query extends TwasiCustomResolver {
    public PanelDTO panel(String token) {
        User user = getUser(token);

        if (user == null) {
            return null;
        }

        return new PanelDTO(user);
    }
}