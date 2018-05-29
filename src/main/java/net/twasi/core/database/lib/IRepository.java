package net.twasi.core.database.lib;

import org.bson.types.ObjectId;

import java.util.List;

public interface IRepository<T> {
    T getById(ObjectId id);
    T getById(String id);
    List<T> getAll();

    void add(T entity);

    boolean commit(T entity);
    void commitAll();
}
