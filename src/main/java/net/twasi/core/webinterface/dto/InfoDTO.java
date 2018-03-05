package net.twasi.core.webinterface.dto;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.User;

public class InfoDTO extends ApiDTO {
    private String twitchid;
    private String username;
    private AccountStatus accountStatus;
    private String avatar;


    public InfoDTO(boolean status, User user) {
        super(status);
        this.twitchid = user.getTwitchAccount().getTwitchId();
        this.username = user.getTwitchAccount().getUserName();
        this.avatar = user.getTwitchAccount().getAvatar();
        this.accountStatus = user.getStatus();
    }
}
