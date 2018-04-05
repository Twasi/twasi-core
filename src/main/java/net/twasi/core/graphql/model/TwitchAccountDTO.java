package net.twasi.core.graphql.model;

public class TwitchAccountDTO {
    private String name;
    private String twitchid;
    private String avatar;
    private String email;

    public TwitchAccountDTO(String name, String twitchid, String avatar, String email) {
        this.name = name;
        this.twitchid = twitchid;
        this.avatar = avatar;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getTwitchid() {
        return twitchid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }
}
