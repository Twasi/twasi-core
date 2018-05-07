package net.twasi.core.database.lib;

import net.twasi.core.database.models.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class EntityCache<T extends BaseEntity> {
    private List<T> cache;

    public EntityCache(List<T> entities) {
        this.cache = entities;
    }

    public EntityCache() {
        this.cache = new ArrayList<>();
    }

    public void add(T entity) {
        if (exist(entity)) {
            throw new IllegalArgumentException("Given entity is already in cache.");
        }
        cache.add(entity);
    }

    public boolean exist(T entity) {
        return cache.stream().anyMatch(item -> item.isSame(entity));
    }

    public void remove(T entity) {
        if (!exist(entity)) {
            throw new IllegalArgumentException("Given entity is not in cache.");
        }
        cache.remove(entity);
    }

    public T get(T term) {
        if (!exist(term)) {
            throw new IllegalArgumentException("Given entity is not in cache.");
        }
        return cache.stream().filter(item -> item.isSame(term)).findFirst().get();
    }

    public void clear() {
        cache.clear();
    }

    public List<T> getAll() {
        return cache;
    }

}
