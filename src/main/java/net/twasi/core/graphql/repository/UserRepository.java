package net.twasi.core.graphql.repository;

import net.twasi.core.graphql.model.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<UserDTO> users;

    public UserRepository() {
        users = new ArrayList<>();
        //add some users to start off with
        users.add(new UserDTO("ID1", "Lars"));
        users.add(new UserDTO("ID2", "Test"));
    }

    public List<UserDTO> getAllUsers() {
        return users;
    }

    public void saveUser(UserDTO link) {
        users.add(link);
    }

}
