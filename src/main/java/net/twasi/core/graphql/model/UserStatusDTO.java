package net.twasi.core.graphql.model;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;

public class UserStatusDTO {
    private User user;

    public UserStatusDTO(User user) {
        this.user = user;
    }

    public UserStatusType getStatus() {
        // User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getById(userId);
        ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);

        if (user.getStatus() == AccountStatus.SETUP) {
            return UserStatusType.SETUP;
        }
        if (user.getStatus() == AccountStatus.BANNED) {
            return UserStatusType.BANNED;
        }
        if (user.getStatus() == AccountStatus.OK || user.getStatus() == AccountStatus.EMAIL_CONFIRMATION) {
            return UserStatusType.OK;
        }
        return UserStatusType.SETUP;
    }

    public enum UserStatusType {
        OK,
        SETUP,
        MIGRATION,
        BANNED,
        MAINTENANCE
    }
}
