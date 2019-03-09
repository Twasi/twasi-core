package net.twasi.core.graphql;

import net.twasi.core.database.models.User;
import net.twasi.core.graphql.model.PanelDTO;
import net.twasi.core.logger.TwasiLogger;

public class Query extends TwasiCustomResolver {
    public PanelDTO panel(String token) {
        User user = getUser(token);

        if (user == null) {
            return null;
        }

        try {
            return new PanelDTO(user);
        } catch (Exception e) {
            TwasiLogger.log.debug(e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
}