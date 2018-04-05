package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.database.models.User;
import net.twasi.core.graphql.model.*;
import net.twasi.core.graphql.repository.UserRepository;
import net.twasi.core.services.JWTService;

public class Query implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    public Query(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ViewerDTO viewer(String token) {
        User user = JWTService.getService().getUserFromToken(token);

        if (user == null) {
            return null;
        }

        UserDTO userDTO = UserDTO.fromUser(user);

        return new ViewerDTO(userDTO, new BotStatusDTO(true));
    }
}