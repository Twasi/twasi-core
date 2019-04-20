package net.twasi.core.graphql.model;

import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.JWTService;

public class AdminDTO {
    private User user;

    public AdminDTO(User user) {
        this.user = user;
    }

    public String impersonate(String twitchname) {
        JWTService service = ServiceRegistry.get(JWTService.class);

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchName(twitchname);

        if (user == null) {
            return null;
        }

        return service.getManager().createNewToken(user);
    }
}
