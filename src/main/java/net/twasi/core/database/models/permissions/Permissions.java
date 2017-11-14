package net.twasi.core.database.models.permissions;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.permissions.PermissionEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.NotSaved;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Permissions {

    @Id
    private ObjectId id;
    private List<PermissionEntity> entities;
    private List<String> keys;
    private String name;

    public Permissions() {}

    public Permissions(List<PermissionEntity> entities, List<String> keys, String name) {
        this.entities = entities;
        this.keys = keys;
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<PermissionEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<PermissionEntity> entities) {
        this.entities = entities;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasPermission(TwitchAccount account, String permissionKey) {
        if (!getKeys().contains(permissionKey)) {
            return false;
        }
        for (PermissionEntity entity : getEntities()) {
            if (entity.getType() == PermissionEntityType.SINGLE) {
                if (entity.getAccount().getTwitchId().equals(account.getTwitchId())) {
                    return true;
                }
            }
            if (entity.getType() == PermissionEntityType.GROUP) {
                if (account.getGroups().contains(entity.getGroup())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doAllKeysExist(ArrayList<String> keys) {
        List<String> toCheck = (List<String>)keys.clone();

        for (String key : getKeys()) {
            if (toCheck.contains(key)) {
                toCheck.remove(key);
            }
        }

        return toCheck.size() == 0;
    }
}
