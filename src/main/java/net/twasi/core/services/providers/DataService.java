package net.twasi.core.services.providers;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.services.IService;

import java.util.HashMap;
import java.util.Map;

public class DataService implements IService {
    private Map<Class, Repository> repos = new HashMap<>();

    public <T extends Repository> T get(Class<T> clazz) {
        if (!repos.containsKey(clazz)) {
            try {
                repos.put(clazz, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) repos.get(clazz);
    }
}
