package net.twasi.core.database.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import net.twasi.twitchapi.auth.AuthenticationType;
import net.twasi.twitchapi.auth.PersonalAuthorizationContext;
import net.twasi.twitchapi.id.oauth2.response.TokenDTO;

public class AccessToken {

    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private String[] scope;

    public AccessToken() {
    }

    public AccessToken(String accessToken, String refreshToken, int expiresIn, String[] scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken(TokenDTO token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.expiresIn = token.getExpiresIn();
        this.scope = token.getScope();
    }

    public String getAccessToken() {
        return accessToken;
    }

    @JsonSetter("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonSetter("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @JsonSetter("expires_in")
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String[] getScope() {
        return scope;
    }

    @JsonSetter("scope")
    public void setScope(String[] scope) {
        this.scope = scope;
    }

    public PersonalAuthorizationContext toAuthContext() {
        return new TwasiPersonalAuthorizationContext(this);
    }

    public class TwasiPersonalAuthorizationContext extends PersonalAuthorizationContext {

        public TwasiPersonalAuthorizationContext(AccessToken token) {
            super(token.getAccessToken(), token.getRefreshToken(), AuthenticationType.BEARER);
        }

        @Override
        public void onRefresh() {
            setAccessToken(getAccessToken());
            setRefreshToken(getRefreshToken());

            // TODO save to db
        }
    }
}
