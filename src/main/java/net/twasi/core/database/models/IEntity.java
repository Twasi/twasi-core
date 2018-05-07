package net.twasi.core.database.models;

public interface IEntity {
    <T extends BaseEntity> boolean isSame(T entity);
}
