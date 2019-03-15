package net.twasi.core.graphql.model.support;

import net.twasi.core.database.models.User;

public class SupportTicketUserDTO {

    private User user;

    public SupportTicketUserDTO(User user) {
        this.user = user;
    }

    public String getName() {
        return user.getTwitchAccount().getDisplayName();
    }

    public String getAvatar() {
        return user.getTwitchAccount().getAvatar();
    }

}
