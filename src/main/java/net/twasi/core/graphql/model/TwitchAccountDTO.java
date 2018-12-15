package net.twasi.core.graphql.model;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.twitchapi.TwitchAPI;
import net.twasi.twitchapi.helix.users.response.UserDTO;

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
        UserDTO userData = TwitchAPI.helix().users().withAuth(account.getToken().toAuthContext(account)).getCurrentUser();
        this.account = TwitchAccount.fromUser(userData, account.getToken());

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchId(account.getTwitchId());
        user.setTwitchAccount(account);
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);

        return this;
    }
}
