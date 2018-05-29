package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.database.models.User;
import net.twasi.core.graphql.model.*;
import net.twasi.core.graphql.repository.UserRepository;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.JWTService;

public class Query implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    public Query(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ViewerDTO viewer(String token) {
        try {
            User user = ServiceRegistry.get(JWTService.class).getManager().getUserFromToken(token);
            try {
                ServiceRegistry.get(DataService.class).get(net.twasi.core.database.repositories.UserRepository.class).commit(user);
            } catch (IllegalArgumentException ignored) {}

            if (user == null) {
                return null;
            }

            UserDTO userDTO = UserDTO.fromUser(user);

            try {
                ServiceRegistry.get(DataService.class).get(net.twasi.core.database.repositories.UserRepository.class).commit(user);
            } catch (IllegalArgumentException ignored) {}

            return new ViewerDTO(userDTO, new BotStatusDTO(user), new UserStatusDTO(user.getId()));
        } catch (Throwable t) {
            TwasiLogger.log.error("Fatal Error in GraphQL.", t);
            return null;
        }
    }
}