package net.twasi.core.webinterface.dto.auth;

import com.google.gson.annotations.SerializedName;
import net.twasi.core.database.models.AccessToken;

public class AccessTokenDTO {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("scope")
    private String[] scope;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public String[] getScope() {
        return scope;
    }

    public AccessToken toModel() {
        return new AccessToken(accessToken, refreshToken, expiresIn, scope);
    }
}
