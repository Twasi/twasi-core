package net.twasi.core.webinterface.dto.auth;

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

        @SerializedName("email")
        private String email;

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

        public String getEmail() {
            return email;
        }
    }

    public TokenDTO getToken() {
        return token;
    }
}
