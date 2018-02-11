package net.twasi.core.database.models.permissions;

import net.twasi.core.database.models.TwitchAccount;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Permissions {

    @Id
    private ObjectId id;
    private List<PermissionEntity> entities;
    private List<String> keys;

    private String name;

    public Permissions() {
    }

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

    /**
     * Checks if a permission key is contained in this permission. If it is, it checks if the twitch account has access.
     *
     * Allows the use of wildcards:
     * commands.mod.add -> commands.mod.*
     *
     * @param account The twitchaccount to check permission for
     * @param permissionKey The key to check
     * @return if the user has permission for the given key
     */
    public boolean hasPermission(TwitchAccount account, String permissionKey) {
        Boolean accountIsContained = false;
        for (PermissionEntity entity : getEntities()) {
            if (entity.getType() == PermissionEntityType.SINGLE) {
                if (entity.getAccount().getTwitchId().equals(account.getTwitchId())) {
                    accountIsContained = true;
                }
            }
            if (entity.getType() == PermissionEntityType.GROUP) {
                if (account.getGroups().contains(entity.getGroup())) {
                    accountIsContained = true;
                }
            }
        }

        if (!accountIsContained) {
            // The account isn't even known in this permission, how can he have it granted?
            return false;
        }

        for(String key : getKeys()) {
            if (key.equalsIgnoreCase(permissionKey)) {
                // Perfect match
                return true;
            }

            String[] keyGroups = key.split("\\.");
            String[] inputGroups = permissionKey.split("\\.");

            if (keyGroups.length < inputGroups.length) {
                continue;
            }

            for(int i = 0; i < keyGroups.length; i++) {
                if (keyGroups[i].equals("*")) {
                    // The group matches (Wildcard)
                    return true;
                }
                if (!keyGroups[i].equalsIgnoreCase(inputGroups[i])) {
                    // Something doesn't match
                    break;
                }
            }
        }
        return false;
    }

    public void addKey(String key) {
        getKeys().add(key);
    }

    public void removeKey(String key) {
        getKeys().remove(key);
    }
}
