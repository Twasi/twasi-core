package net.twasi.core.graphql.model;

import net.twasi.core.database.models.Language;
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
        this.user.getConfig().setActivated(isRunning);
        DataService.get().get(UserRepository.class).commit(this.user);
        if (isRunning) {
            ServiceRegistry.get(InstanceManagerService.class).start(user);
        } else {
            ServiceRegistry.get(InstanceManagerService.class).stop(user);
        }
        return this;
    }

    public boolean setLanguage(String languageCode) {
        try {
            user.getConfig().setLanguage(Language.valueOf(languageCode));
            UserRepository repo = DataService.get().get(UserRepository.class);
            repo.commit(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
