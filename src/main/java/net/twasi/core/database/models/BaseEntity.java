package net.twasi.core.database.models;

import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

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
