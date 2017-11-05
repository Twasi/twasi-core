package net.twasi.core.webinterface.dto.auth;

import com.google.gson.annotations.SerializedName;
import net.twasi.core.database.models.AccessToken;

public class AccessTokenDTO {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("expires_in")
    public int expiresIn;

    @SerializedName("scope")
    public String[] scope;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String[] getScope() {
        return scope;
    }

    public AccessToken toModel() {
        return new AccessToken(accessToken, refreshToken, expiresIn, scope);
    }
}
