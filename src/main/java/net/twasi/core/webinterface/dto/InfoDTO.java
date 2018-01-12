package net.twasi.core.webinterface.dto;

import net.twasi.core.database.models.AccountStatus;

public class InfoDTO extends ApiDTO {
    private String twitchid;
    private String username;
    private AccountStatus accountStatus;


    public InfoDTO(boolean status, String twitchid, String username, AccountStatus accountStatus) {
        super(status);
        this.twitchid = twitchid;
        this.username = username;
        this.accountStatus = accountStatus;
    }
}
