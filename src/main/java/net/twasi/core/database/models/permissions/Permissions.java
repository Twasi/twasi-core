package net.twasi.core.database.models.permissions;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

public class Permissions {

    @Id
    private ObjectId id;

    private List<PermissionEntity> entities;
    private List<String> keys;
    private String name;

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
}
