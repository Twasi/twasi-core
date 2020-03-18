package net.twasi.core.api.ws.models;

import net.twasi.core.database.models.User;
import net.twasi.core.services.providers.JWTService;

import java.util.ArrayList;
import java.util.List;

public class TwasiWebsocketAuthentication {

    private User user = null;
    private List<String> topics = new ArrayList<>();

    public TwasiWebsocketAuthentication(AuthenticationType type, String token) throws WebsocketHandledException {
        this.authenticate(type, token);
    }

    public void authenticate(AuthenticationType type, String token) throws WebsocketHandledException {
        if (type.equals(AuthenticationType.JWT_TOKEN)) {
            if ((user = JWTService.get().getManager().getUserFromToken(token)) == null)
                throw new WebsocketHandledException("The JWT token provided does not belong to any user or is invalid");
        } else if (type.equals(AuthenticationType.EXPLICIT_GRANT_TOKEN)) {
            // TODO Not implemented yet
        }
    }

    public enum AuthenticationType {
        JWT_TOKEN, // Used to grant all permissions of a user by JWT token
        EXPLICIT_GRANT_TOKEN // Used to grant access to specific topics by explicit grant tokens
    }

    public User getUser() {
        return user;
    }

    public List<String> getTopics() {
        return topics;
    }
}
