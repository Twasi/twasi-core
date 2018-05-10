package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.services.providers.InstanceManagerService;

public class BotStatusDTO {
    private User user;

    public BotStatusDTO(User user) {
        this.user = user;
    }

    public boolean isRunning() {
        return InstanceManagerService.getService().hasRegisteredInstance(user);
    }

    public BotStatusDTO changeStatus(Boolean isRunning) {
        if (isRunning) {
            InstanceManagerService.getService().start(user);
        } else {
            InstanceManagerService.getService().stop(user);
        }
        return this;
    }
}
