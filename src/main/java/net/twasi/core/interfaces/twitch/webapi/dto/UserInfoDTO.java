package net.twasi.core.interfaces.twitch.webapi.dto;

import com.google.gson.annotations.SerializedName;

public class UserInfoDTO {

    private String email;
    private String logo;

    @SerializedName("_id")
    private String twitchId;

    public String getEmail() {
        return email;
    }

    public String getLogo() {
        return logo;
    }
}
