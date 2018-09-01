package net.twasi.core.plugin.java;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.Plugin;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.PluginLoader;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.PluginManagerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
 */
public final class JavaPluginLoader implements PluginLoader {
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<>();

    public TwasiPlugin loadPlugin(final File file) throws Exception {
        if (file == null) {
            throw new Exception("File can't be null");
        }

        if (!file.exists()) {
            throw new Exception(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        final PluginConfig description;
        description = getPluginConfig(file);

        final File parentFile = file.getParentFile();
        final File dataFolder = new File(parentFile, description.getName());

        // TODO: We need to handle it when a plugin is loaded before another one. Maybe perform a sanity check shortly after loading all plugins? (PluginDiscovery)
        for (String pluginName : description.getDependencies()) {
            Plugin dependency = ServiceRegistry.get(PluginManagerService.class).getByName(pluginName);

            if (dependency == null) {
                TwasiLogger.log.warn("Could not resolve dependency '" + pluginName + "' of plugin '" + description.getName() + "'");
            }
        }

        final PluginClassLoader loader;
        try {
            loader = new PluginClassLoader(this, getClass().getClassLoader(), description, dataFolder, file);
        } catch (Exception ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new Exception(ex);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    public PluginConfig getPluginConfig(File file) throws Exception {
        if (file == null) {
            throw new Exception("File can't be null");
        }

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new Exception(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(entry);

            return PluginConfig.fromInputStream(stream);
        } catch (IOException ex) {
            throw new Exception(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (PluginClassLoader loader : loaders) {
                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException cnfe) {
                }
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    void setClass(final String name, final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);

            /* if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            } */
        }
    }

    private void removeClass(String name) {
        classes.remove(name);

        /* try {
            if ((clazz != null) && (ConfigurationSerializable.class.isAssignableFrom(clazz))) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.unregisterClass(serializable);
            }
        } catch (NullPointerException ex) {
            // Boggle!
            // (Native methods throwing NPEs is not fun when you can't stop it before-hand)
        } */
    }

    /* public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, final Plugin plugin) {
        Validate.notNull(plugin, "Plugin can not be null");
        Validate.notNull(listener, "Listener can not be null");

        boolean useTimings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredListener>>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<Method>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods) {
                methods.add(method);
            }
            for (Method method : privateMethods) {
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().severe("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.getLogger().severe(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<RegisteredListener>();
                ret.put(eventClass, eventSet);
            }

            for (Class<?> clazz = eventClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                // This loop checks for extending deprecated events
                if (clazz.getAnnotation(Deprecated.class) != null) {
                    Warning warning = clazz.getAnnotation(Warning.class);
                    WarningState warningState = server.getWarningState();
                    if (!warningState.printFor(warning)) {
                        break;
                    }
                    plugin.getLogger().log(
                            Level.WARNING,
                            String.format(
                                    "\"%s\" has registered a listener for %s on method \"%s\", but the event is Deprecated." +
                                            " \"%s\"; please notify the authors %s.",
                                    plugin.getDescription().getFullName(),
                                    clazz.getName(),
                                    method.toGenericString(),
                                    (warning != null && warning.reason().length() != 0) ? warning.reason() : "Server performance will be affected",
                                    Arrays.toString(plugin.getDescription().getAuthors().toArray())),
                            warningState == WarningState.ON ? new AuthorNagException(null) : null);
                    break;
                }
            }

            EventExecutor executor = new EventExecutor() {
                public void execute(Listener listener, Event event) throws EventException {
                    try {
                        if (!eventClass.isAssignableFrom(event.getClass())) {
                            return;
                        }
                        method.invoke(listener, event);
                    } catch (InvocationTargetException ex) {
                        throw new EventException(ex.getCause());
                    } catch (Throwable t) {
                        throw new EventException(t);
                    }
                }
            };
            if (useTimings) {
                eventSet.add(new TimedRegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            } else {
                eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            }
        }
        return ret;
    } */

    public void enablePlugin(final Plugin plugin) {
        if (!(plugin instanceof TwasiPlugin)) {
            plugin.getLogger().error("Plugin is not associated with this PluginLoader");
        }
        ;

        if (!plugin.isActivated()) {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getName());

            TwasiPlugin tPlugin = (TwasiPlugin) plugin;

            PluginClassLoader pluginLoader = (PluginClassLoader) tPlugin.getClassLoader();

            if (!loaders.contains(pluginLoader)) {
                loaders.add(pluginLoader);
                TwasiLogger.log.warn("Enabled plugin with unregistered PluginClassLoader " + plugin.getDescription().getName());
            }

            try {
                tPlugin.setActivated(true);
            } catch (Throwable ex) {
                TwasiLogger.log.error("Error occurred while activating " + plugin.getDescription().getName() + " (Is it up to date?)", ex);
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
            // server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    public void disablePlugin(Plugin plugin) {
        if (!(plugin instanceof TwasiPlugin)) TwasiLogger.log.error("Plugin is not associated with this PluginLoader");

        if (plugin.isActivated()) {
            String message = String.format("Disabling %s", plugin.getDescription().getName());
            plugin.getLogger().info(message);
            // server.getPluginManager().callEvent(new PluginDisableEvent(plugin));

            TwasiPlugin tPlugin = (TwasiPlugin) plugin;
            ClassLoader cloader = tPlugin.getClassLoader();

            try {
                tPlugin.setActivated(false);
            } catch (Throwable ex) {
                TwasiLogger.log.error("Error occurred while disabling " + plugin.getDescription().getName() + " (Is it up to date?)", ex);
            }

            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader) cloader;
                loaders.remove(loader);

                Set<String> names = loader.getClasses();

                for (String name : names) {
                    removeClass(name);
                }
            }
        }
    }
}