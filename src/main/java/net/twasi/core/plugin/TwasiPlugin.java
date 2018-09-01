package net.twasi.core.plugin;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.graphql.WebInterfaceApp;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.java.JavaPluginLoader;
import net.twasi.core.plugin.java.PluginClassLoader;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Represents a Java plugin
 */
public abstract class TwasiPlugin extends PluginBase {
    private boolean isEnabled = false;
    private PluginLoader loader = null;
    private File file = null;
    private PluginConfig description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;
    private Logger logger = null;

    public TwasiPlugin() {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        if (!(classLoader instanceof PluginClassLoader)) {
            throw new IllegalStateException("TwasiPlugin requires " + PluginClassLoader.class.getName());
        }
        ((PluginClassLoader) classLoader).initialize(this);
    }

    protected TwasiPlugin(final JavaPluginLoader loader, final PluginConfig description, final File file) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader instanceof PluginClassLoader) {
            throw new IllegalStateException("Cannot use initialization constructor at runtime");
        }
        init(loader, description, dataFolder, file, classLoader);
    }

    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder.
     */
    @Override
    public final File getDataFolder() {
        return dataFolder;
    }

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    @Override
    public final PluginLoader getPluginLoader() {
        return loader;
    }

    /**
     * Returns a value indicating whether or not this plugin is currently
     * enabled
     *
     * @return true if this plugin is enabled, otherwise false
     */
    @Override
    public final boolean isActivated() {
        return isEnabled;
    }

    /**
     * Returns the file which contains this plugin
     *
     * @return File containing this plugin
     */
    protected File getFile() {
        return file;
    }

    /**
     * Returns the plugin.yaml file containing the details for this plugin
     *
     * @return Contents of the plugin.yaml file
     */
    @Override
    public final PluginConfig getDescription() {
        return description;
    }

    @Override
    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Returns the ClassLoader which holds this plugin
     *
     * @return ClassLoader holding this plugin
     */
    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Sets the enabled state of this plugin
     *
     * @param enabled true if enabled, otherwise false
     */
    public final void setActivated(final boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (isEnabled) {
                onActivate();
            } else {
                onDeactivate();
            }
        }
    }


    public final void init(PluginLoader loader, PluginConfig description, File dataFolder, File file, ClassLoader classLoader) {
        this.loader = loader;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.logger = TwasiLogger.log;
    }

    @Override
    public void onDeactivate() {
    }

    @Override
    public void onActivate() {
    }

    @Override
    public final Logger getLogger() {
        return logger;
    }

    @Override
    public void registerRoute(String path, RequestHandler handler) {
        WebInterfaceApp.getHandlers().addHandler(handler);
    }

    @Override
    public GraphQLQueryResolver getGraphQLResolver() {
        return null;
    }

    @Override
    public String toString() {
        return description.getName();
    }

    /**
     * This method provides fast access to the plugin that has {@link
     * #getProvidingPlugin(Class) provided} the given plugin class, which is
     * usually the plugin that implemented it.
     * <p>
     * An exception to this would be if plugin's jar that contained the class
     * does not extend the class, where the intended plugin would have
     * resided in a different jar / classloader.
     *
     * @param <T>   a class that extends JavaPlugin
     * @param clazz the class desired
     * @return the plugin that provides and implements said class
     * @throws IllegalArgumentException if clazz is null
     * @throws IllegalArgumentException if clazz does not extend {@link
     *                                  TwasiPlugin}
     * @throws IllegalStateException    if clazz was not provided by a plugin,
     *                                  for example, if called with
     *                                  <code>JavaPlugin.getPlugin(JavaPlugin.class)</code>
     * @throws IllegalStateException    if called from the static initializer for
     *                                  given JavaPlugin
     * @throws ClassCastException       if plugin that provided the class does not
     *                                  extend the class
     */
    public static <T extends TwasiPlugin> T getPlugin(Class<T> clazz) {
        if (clazz == null) {
            TwasiLogger.log.error("Null class cannot have a plugin");
        }
        ;
        if (!TwasiPlugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz + " does not extend " + TwasiPlugin.class);
        }
        final ClassLoader cl = clazz.getClassLoader();
        if (!(cl instanceof PluginClassLoader)) {
            throw new IllegalArgumentException(clazz + " is not initialized by " + PluginClassLoader.class);
        }
        TwasiPlugin plugin = ((PluginClassLoader) cl).plugin;
        if (plugin == null) {
            throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
        }
        return clazz.cast(plugin);
    }

    /**
     * This method provides fast access to the plugin that has provided the
     * given class.
     *
     * @param clazz a class belonging to a plugin
     * @return the plugin that provided the class
     * @throws IllegalArgumentException if the class is not provided by a
     *                                  JavaPlugin
     * @throws IllegalArgumentException if class is null
     * @throws IllegalStateException    if called from the static initializer for
     *                                  given JavaPlugin
     */
    public static TwasiPlugin getProvidingPlugin(Class<?> clazz) {
        if (clazz == null) {
            TwasiLogger.log.error("Null class cannot have a plugin");
        }
        final ClassLoader cl = clazz.getClassLoader();
        if (!(cl instanceof PluginClassLoader)) {
            throw new IllegalArgumentException(clazz + " is not provided by " + PluginClassLoader.class);
        }
        TwasiPlugin plugin = ((PluginClassLoader) cl).plugin;
        if (plugin == null) {
            throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
        }
        return plugin;
    }
}