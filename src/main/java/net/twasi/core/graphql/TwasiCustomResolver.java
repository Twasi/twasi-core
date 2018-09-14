package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.JWTService;

public abstract class TwasiCustomResolver implements GraphQLQueryResolver {

    protected User getUser(String token) {
        try {
            User user = ServiceRegistry.get(JWTService.class).getManager().getUserFromToken(token);
            try {
                ServiceRegistry.get(DataService.class).get(net.twasi.core.database.repositories.UserRepository.class).commit(user);
            } catch (IllegalArgumentException ignored) {}

            return user;
        } catch (Throwable t) {
            TwasiLogger.log.error("Fatal error in GraphQL.", t);
            return null;
        }
    }

}
