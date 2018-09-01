package net.twasi.core.plugin;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.webinterface.lib.RequestHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;

/**
 * Represents a Plugin
 */
public interface Plugin {
    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    public File getDataFolder();

    /**
     * Returns the plugin.yaml file containing the details for this plugin
     *
     * @return Contents of the plugin.yaml file
     */
    public PluginConfig getDescription();

    /**
     * Gets an embedded resource in this plugin
     *
     * @param filename Filename of the resource
     * @return File if found, otherwise null
     */
    public InputStream getResource(String filename);

    ;

    /**
     * Gets the associated PluginLoader responsible for this plugin
     *
     * @return PluginLoader that controls this plugin
     */
    public PluginLoader getPluginLoader();

    /**
     * Returns a value indicating whether or not this plugin is currently
     * enabled
     *
     * @return true if this plugin is enabled, otherwise false
     */
    public boolean isActivated();

    /**
     * Called when this plugin is disabled
     */
    public void onDeactivate();

    /**
     * Called when this plugin is enabled
     */
    public void onActivate();

    /**
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return Logger associated with this plugin
     */
    public Logger getLogger();

    /**
     * Returns the name of the plugin.
     * <p>
     * This should return the bare name of the plugin and should be used for
     * comparison.
     *
     * @return name of the plugin
     */
    public String getName();

    /**
     * Returns the user Plugin used by this core extension
     *
     * @return the user plugin class
     */
    public Class<? extends TwasiUserPlugin> getUserPluginClass();

    /**
     * Registers a new route to the API
     */
    public void registerRoute(String path, RequestHandler handler);

    public GraphQLQueryResolver getGraphQLResolver();
}
