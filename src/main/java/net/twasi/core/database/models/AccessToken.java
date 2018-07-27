package net.twasi.core.database.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import net.twasi.core.interfaces.twitch.webapi.TwitchAPI;
import net.twasi.core.services.ServiceRegistry;

import java.util.Calendar;

public class AccessToken {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String[] scope;

    public AccessToken() {
    }

    public AccessToken(String accessToken, String refreshToken, Long expiresIn, String[] scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public Long getExpiresIn() {
        return expiresIn;
    }

    @JsonSetter("expires_in")
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String[] getScope() {
        return scope;
    }

    @JsonSetter("scope")
    public void setScope(String[] scope) {
        this.scope = scope;
    }

    public OAuthCredential toCredential() {
        OAuthCredential cred = new OAuthCredential();
        cred.setToken(getAccessToken());
        cred.setRefreshToken(getRefreshToken());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getExpiresIn());

        cred.setTokenExpiresAt(cal);
        return cred;
    }

    public void refresh() {
        AccessToken newToken = ServiceRegistry.get(TwitchAPI.class).refreshToken(this);
        setAccessToken(newToken.getAccessToken());
        setRefreshToken(newToken.getRefreshToken());
        setScope(newToken.getScope());
        setExpiresIn(newToken.getExpiresIn());
    }
}
