package net.twasi.core.webinterface.dto;

public class UserPostDTO {

    private TwitchAccountDTO twitchAccount;

    public boolean isValid() {
        return twitchAccount.isValid();
    }

}
