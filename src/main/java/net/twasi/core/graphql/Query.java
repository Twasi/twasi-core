package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.database.models.User;
import net.twasi.core.graphql.model.*;
import net.twasi.core.graphql.repository.UserRepository;
import net.twasi.core.services.JWTService;

import java.util.ArrayList;

public class Query implements GraphQLQueryResolver {

    private final UserRepository userRepository;

    public Query(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ViewerDTO viewer(String token) {
        UserDTO userDTO = new UserDTO("MyId", new TwitchAccountDTO("Name", "Twitchid", "Avagar", "Email"), new ArrayList<>(), new ArrayList<>());

        User user = JWTService.getService().getUserFromToken(token);

        if (user == null) {
            return null;
        }

        return new ViewerDTO(userDTO, new BotStatusDTO(true));
    }
}