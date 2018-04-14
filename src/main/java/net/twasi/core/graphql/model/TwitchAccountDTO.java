package net.twasi.core.graphql.model;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.services.TwitchAPIService;

public class TwitchAccountDTO {
    private TwitchAccount account;

    public TwitchAccountDTO(TwitchAccount account) {
        this.account = account;
    }

    public String getName() {
        return account.getUserName();
    }

    public String getTwitchid() {
        return account.getTwitchId();
    }

    public String getAvatar() {
        return account.getAvatar();
    }

    public String getEmail() {
        return account.getEmail();
    }

    public String getDisplayName() {
        return account.getDisplayName();
    }

    public TwitchAccountDTO update() {
        this.account = TwitchAPIService.getService().getTwitchAccountByToken(account.getToken());

        User user = UserStore.getById(account.getTwitchId());
        user.setTwitchAccount(account);
        UserStore.updateUser(user);

        return this;
    }
}
