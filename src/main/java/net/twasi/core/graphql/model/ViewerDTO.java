package net.twasi.core.graphql.model;

public class ViewerDTO {
    private UserDTO user;
    private BotStatusDTO status;

    public ViewerDTO(UserDTO user, BotStatusDTO status) {
        this.user = user;
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public BotStatusDTO getStatus() {
        return status;
    }
}
