package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.graphql.model.UserDTO;
import net.twasi.core.graphql.repository.UserRepository;

import java.util.List;

public class Query implements GraphQLRootResolver {

    private final UserRepository userRepository;

    public Query(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> allUsers() {
        return userRepository.getAllUsers();
    }
}