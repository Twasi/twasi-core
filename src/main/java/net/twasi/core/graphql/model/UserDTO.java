package net.twasi.core.graphql.model;

public class UserDTO {

    private final String id;
    private final String name;

    public UserDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

}
