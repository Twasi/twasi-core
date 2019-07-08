package net.twasi.core.api.oauth;

import net.twasi.core.database.models.User;

public class OAuthState {

    public User user;
    public String environment;

    public OAuthState(User user, String redirectUri) {
        this.user = user;
        this.environment = redirectUri;
    }
}
