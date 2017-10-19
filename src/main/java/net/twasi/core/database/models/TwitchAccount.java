package net.twasi.core.database.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("twitchAccounts")
public class TwitchAccount {
    @Id
    private ObjectId id;
    private String userName;
    private String token;

    public TwitchAccount() {}

    public TwitchAccount(String userName, String token) {
        this.userName = userName;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannel() {
        return "#" + this.userName;
    }
}
