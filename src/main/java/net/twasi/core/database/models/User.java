package net.twasi.core.database.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("users")
public class User {
    @Id
    private ObjectId id;
    private String email;
    private TwitchAccount twitchAccount;

    public User(String email) {
        this.email = email;
    }

    public User() {};

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TwitchAccount getTwitchAccount() {
        return twitchAccount;
    }

    public void setTwitchAccount(TwitchAccount twitchAccount) {
        this.twitchAccount = twitchAccount;
    }
}
