package net.twasi.core.interfaces.twitch.webapi.dto;

import com.google.gson.annotations.SerializedName;

public class TokenInfoDTO {

    private TokenDTO token;

    public class TokenDTO {
        @SerializedName("client_id")
        private String clientId;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("user_name")
        private String userName;

        @SerializedName("valid")
        private boolean valid;

        public String getClientId() {
            return clientId;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public boolean isValid() {
            return valid;
        }
    }

    public TokenDTO getToken() {
        return token;
    }
}
