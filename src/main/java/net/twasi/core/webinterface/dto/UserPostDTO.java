package net.twasi.core.webinterface.dto;

public class UserPostDTO {

    public TwitchAccountDTO twitchAccount;

    public boolean isValid() {
        return twitchAccount.isValid();
    }

}
