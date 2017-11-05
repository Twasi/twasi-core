package net.twasi.core.database.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("twitchAccounts")
public class TwitchAccount {
    @Id
    private ObjectId id;
    private String userName;
    private AccessToken token;
    private String twitchId;

    public TwitchAccount() {}

    public TwitchAccount(String userName, AccessToken token, String twitchId) {
        this.userName = userName;
        this.token = token;
        this.twitchId = twitchId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setToken(AccessToken token) {
        this.token = token;
    }

    public String getChannel() {
        return "#" + this.userName;
    }

    public String getTwitchId() {
        return twitchId;
    }

    public void setTwitchId(String twitchId) {
        this.twitchId = twitchId;
    }
}
