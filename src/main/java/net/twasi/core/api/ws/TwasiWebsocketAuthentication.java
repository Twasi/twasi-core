package net.twasi.core.api.ws;

import net.twasi.core.database.models.User;
import net.twasi.core.services.providers.JWTService;

import java.util.ArrayList;
import java.util.List;

public class TwasiWebsocketAuthentication {

    private User user = null;
    private List<String> topics = new ArrayList<>(); // User is granted all topic

    public TwasiWebsocketAuthentication(AuthenticationType type, String token) throws RuntimeException {
        if (type.equals(AuthenticationType.JWT_TOKEN)) {
            if ((user = JWTService.get().getManager().getUserFromToken(token)) == null)
                throw new RuntimeException();
        } else if (type.equals(AuthenticationType.EXPLICIT_GRANT_TOKEN)) {
            // TODO Not implemented yet
        }
    }

    public enum AuthenticationType {
        JWT_TOKEN, // Used to grant all permissions of a user by JWT token
        EXPLICIT_GRANT_TOKEN // Used to grant access to specific topics by explicit grant tokens
    }

}
