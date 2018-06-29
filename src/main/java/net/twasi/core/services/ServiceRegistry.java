package net.twasi.core.services;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {
    private static Map<Class, IService> services = new HashMap<>();

    public static <T extends IService> T getService(Class<T> clazz) {
        if (!services.containsKey(clazz)) {
            throw new IllegalArgumentException("Given class " + clazz.getName() + " has no registered service instance.");
        }
        return (T)services.get(clazz);
    }

    public static <T extends IService> T get(Class<T> clazz) {
        return getService(clazz);
    }

    public static <T extends IService> void register(T service) {
        if (services.containsKey(service.getClass())) {
            throw new IllegalArgumentException("Given class " + service.getClass() + " does already have a registered instance.");
        }
        services.put(service.getClass(), service);
    }

    public static boolean has(Class clazz) {
        return services.containsKey(clazz);
    }
}
