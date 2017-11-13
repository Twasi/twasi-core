package net.twasi.core.database.models.permissions;

import net.twasi.core.database.models.TwitchAccount;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class PermissionEntity {

    @Id
    private ObjectId id;

    private PermissionEntityType type;

    private PermissionGroups group;
    private TwitchAccount account;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public PermissionEntityType getType() {
        return type;
    }

    public void setType(PermissionEntityType type) {
        this.type = type;
    }

    public PermissionGroups getGroup() {
        return group;
    }

    public void setGroup(PermissionGroups group) {
        this.group = group;
    }

    public TwitchAccount getAccount() {
        return account;
    }

    public void setAccount(TwitchAccount account) {
        this.account = account;
    }
}
