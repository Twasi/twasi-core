package net.twasi.core.graphql.model;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import org.bson.types.ObjectId;

public class UserStatusDTO {
    private ObjectId userId;

    public UserStatusDTO(ObjectId userId) {
        this.userId = userId;
    }

    public UserStatusType getStatus() {
        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getById(userId);
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
