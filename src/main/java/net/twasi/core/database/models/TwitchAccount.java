package net.twasi.core.database.models;

import net.twasi.core.database.models.permissions.PermissionGroups;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity("twitchAccounts")
public class TwitchAccount {
    @Id
    private ObjectId id;
    private String userName;
    private AccessToken token;
    private String twitchId;
    private String avatar;
    private String email;
    private String confirmationCode;

    private List<PermissionGroups> groups;

    public TwitchAccount() {
    }

    public TwitchAccount(String userName, AccessToken token, String twitchId, List<PermissionGroups> groups) {
        this.userName = userName;
        this.token = token;
        this.twitchId = twitchId;
        this.groups = groups;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PermissionGroups> getGroups() {
        return groups;
    }

    public void setGroups(List<PermissionGroups> groups) {
        this.groups = groups;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
