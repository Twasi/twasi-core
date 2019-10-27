package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.JWTService;

public abstract class TwasiCustomResolver implements GraphQLQueryResolver {

    protected boolean enforceSetup = true;

    protected User getUser(String token) {
        User user = getUserRaw(token);

        if (enforceSetup && user.getStatus() == AccountStatus.SETUP) {
            throw new TwasiGraphQLHandledException("You can't perform that action. Account is not set up yet.", "account.nosetup");
        }

        return user;
    }

    protected User getUserRaw(String token) {
        try {
            User user = ServiceRegistry.get(JWTService.class).getManager().getUserFromToken(token);

            if (user == null) {
                // User not found for token
                return null;
            }

            try {
                ServiceRegistry.get(DataService.class).get(net.twasi.core.database.repositories.UserRepository.class).commit(user);
            } catch (IllegalArgumentException ignored) {
            }

            return user;
        } catch (Throwable t) {
            TwasiLogger.log.error("Error while authenticating user.", t);
            return null;
        }
    }
}
