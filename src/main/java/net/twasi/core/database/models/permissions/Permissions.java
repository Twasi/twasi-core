package net.twasi.core.database.models.permissions;

import net.twasi.core.database.models.TwitchAccount;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

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
     * <p>
     * Allows the use of wildcards:
     * commands.mod.add -> commands.mod.*
     *
     * @param account       The twitchaccount to check permission for
     * @param permissionKey The key to check
     * @return if the user has permission for the given key
     */
    public boolean hasPermission(TwitchAccount account, String permissionKey) {
        for (PermissionEntity entity : getEntities()) {
            if (entity.getType() == PermissionEntityType.SINGLE && !entity.getAccount().getTwitchId().equals(account.getTwitchId()))
                return false;
            else if (!account.getGroups().contains(entity.getGroup())) return false;
        }

        for (String key : getKeys()) {
            /* Return true if the user has exactly the permission
               that is requested. */
            if (key.equalsIgnoreCase(permissionKey)) return true;

            String[] keyGroups = key.split("\\.");
            String[] inputGroups = permissionKey.split("\\.");

            /* Go to the next permission if the requested permission
               is shorter than the current key (current key is too specific) */
            if (inputGroups.length < keyGroups.length) continue;

            for (int i = 0; i < keyGroups.length; i++) {
                if (i != (keyGroups.length - 1)) { // If current part is not the last one
                    if (!keyGroups[i].equalsIgnoreCase(inputGroups[i]))
                        break; // If current part does not fit the required permission
                } else { // If current part is the last one
                    if (keyGroups[i].equalsIgnoreCase(inputGroups[i]) || keyGroups[i].equals("*"))
                        return true; // If last part fits or is wildcard (*) return true
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
