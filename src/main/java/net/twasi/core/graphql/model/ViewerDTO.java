package net.twasi.core.graphql.model;

public class ViewerDTO {
    private UserDTO user;
    private BotStatusDTO status;
    private UserStatusDTO userStatus;
    private AppInfoDTO appInfo = new AppInfoDTO();

    public ViewerDTO(UserDTO user, BotStatusDTO status, UserStatusDTO userStatus) {
        this.user = user;
        this.status = status;
        this.userStatus = userStatus;
    }

    public UserDTO getUser() {
        return user;
    }

    public BotStatusDTO getStatus() {
        return status;
    }

    public UserStatusDTO getUserStatus() {
        return userStatus;
    }

    public AppInfoDTO getAppInfo() {
        return appInfo;
    }
}
