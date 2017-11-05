package net.twasi.core.webinterface.dto.auth;

import com.google.gson.annotations.SerializedName;

public class TokenInfoDTO {

    public TokenDTO token;

    public class TokenDTO {
        @SerializedName("client_id")
        public String clientId;

        @SerializedName("user_id")
        public String userId;

        @SerializedName("user_name")
        public String userName;

        @SerializedName("valid")
        public boolean valid;
    }

}
