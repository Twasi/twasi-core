package net.twasi.core.database.models;

import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.interfaces.twitch.webapi.dto.TokenInfoDTO;
import net.twasi.core.interfaces.twitch.webapi.dto.UserInfoDTO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity("twitchAccounts")
public class TwitchAccount {
    @Id
    private ObjectId id;
    private String userName;
    private String displayName;
    private AccessToken token;
    private String twitchId;
    private String avatar;
    private String email;
    private String confirmationCode;

    private List<PermissionGroups> groups;

    public TwitchAccount() {
    }

    public TwitchAccount(String userName, String displayName, AccessToken token, String twitchId, List<PermissionGroups> groups) {
        this.userName = userName;
        this.displayName = displayName;
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
        if (avatar == null) {
            return "https://static-cdn.jtvnw.net/jtv_user_pictures/eb858cdd0224da3d-profile_image-300x300.png";
        }
        return avatar;
    }

    public String getDisplayName() {
        if (displayName == null) {
            return getUserName();
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static TwitchAccount fromUser(UserInfoDTO user, TokenInfoDTO tokenInfo, AccessToken token) {
        TwitchAccount acc = new TwitchAccount();

        acc.setUserName(tokenInfo.getToken().getUserName());
        acc.setDisplayName(user.getDisplayName());
        acc.setAvatar(user.getLogo());
        acc.setTwitchId(String.valueOf(tokenInfo.getToken().getUserId()));
        acc.setEmail(user.getEmail());
        acc.setToken(token);
        return acc;
    }
}
