package net.twasi.core.database.lib;

import net.twasi.core.database.models.BaseEntity;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DatabaseService;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

public class Repository<T extends BaseEntity> implements IRepository<T> {
    protected Datastore store;
    private Class entityType;

    protected EntityCache<T> cache = new EntityCache<>();

    public Repository() {
        store = ServiceRegistry.get(DatabaseService.class).getStore();
        entityType = ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    public T getById(ObjectId id) {
        T entity = (T) store.get(entityType, id);

        if (cache.exist(entity)) {
            // Return from cache
            return cache.get(entity);
        } else {
            // Add to cache
            cache.add(entity);
            return entity;
        }
    }

    @Override
    public T getById(String id) {
        return getById(new ObjectId(id));
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public void add(T entity) {
        store.save(entity);
    }

    @Override
    public boolean commit(T term) {
        if (!cache.exist(term)) {
            TwasiLogger.log.debug("Tried to commit entity that is not in cache.");
            return false;
        }
        T cachedEntity = cache.get(term);
        store.save(cachedEntity);
        cache.remove(cachedEntity);
        return true;
    }

    @Override
    public void commitAll() {
        //TODO: do all at once for performance boost?
        cache.getAll().forEach(this::commit);
    }

    public List<ObjectId> getAllIds() {
        List<ObjectId> ids = (List<ObjectId>) store.find(entityType).asList().stream().map(e -> ((BaseEntity)e).getId()).collect(Collectors.toList());
        return null;
    }
}
