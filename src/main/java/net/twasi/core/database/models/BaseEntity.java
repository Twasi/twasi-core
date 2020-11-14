package net.twasi.core.database.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public abstract class BaseEntity implements IEntity {
    @Id
    private ObjectId id;

    @Override
    public <T extends BaseEntity> boolean isSame(T entity) {
        return entity.getId().equals(getId());
    }

    public ObjectId getId() {
        return id;
    }
}
