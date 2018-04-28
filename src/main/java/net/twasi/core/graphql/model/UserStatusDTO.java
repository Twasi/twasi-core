package net.twasi.core.graphql.model;

import net.twasi.core.database.models.AccountStatus;
import net.twasi.core.database.models.User;

public class UserStatusDTO {
    private UserStatusType status;

    public UserStatusDTO(User user) {
        if (user.getStatus() == AccountStatus.SETUP) {
            this.status = UserStatusType.SETUP;
        }
        if (user.getStatus() == AccountStatus.BANNED) {
            this.status = UserStatusType.BANNED;
        }
        if (user.getStatus() == AccountStatus.OK || user.getStatus() == AccountStatus.EMAIL_CONFIRMATION) {
            this.status = UserStatusType.OK;
        }
    }

    public UserStatusType getStatus() {
        return status;
    }

    public enum UserStatusType {
        OK,
        SETUP,
        MIGRATION,
        BANNED,
        MAINTENANCE
    }
}
