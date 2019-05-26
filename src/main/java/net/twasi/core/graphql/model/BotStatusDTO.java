package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.InstanceManagerService;

public class BotStatusDTO {
    private User user;

    public BotStatusDTO(User user) {
        this.user = user;
    }

    public boolean isRunning() {
        return ServiceRegistry.get(InstanceManagerService.class).hasRegisteredInstance(user);
    }

    public BotStatusDTO changeStatus(Boolean isRunning) {
        if (isRunning) {
            ServiceRegistry.get(InstanceManagerService.class).start(user);
        } else {
            ServiceRegistry.get(InstanceManagerService.class).stop(user);
        }
        this.user.getConfig().setActivated(isRunning());
        DataService.get().get(UserRepository.class).commit(this.user);
        return this;
    }
}
