package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class PluginLoader {

    ArrayList<TwasiPlugin> loadedPlugins = new ArrayList<>();

    public PluginLoader() {

        if (!new File("plugins").isDirectory()) {
            TwasiLogger.log.info("Plugins directory not found, creating.");
            new File("plugins").mkdir();
        }

        File[] pluginJars = new File("plugins").listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        for(File plugin : pluginJars) {
            TwasiLogger.log.debug("Loading plugin " + plugin);

            try {
                TwasiPluginLoader urlCl = new TwasiPluginLoader(System.class.getClassLoader(), plugin);
                loadedPlugins.add(urlCl.plugin);
            } catch (ClassNotFoundException e) {
                TwasiLogger.log.error(e);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
            }
        }
        TwasiLogger.log.info(loadedPlugins.size() + " plugins loaded.");

        TwasiLogger.log.info("Enabling plugins");
        for (TwasiPlugin plugin : loadedPlugins) {
            plugin.onEnable();
        }
        TwasiLogger.log.info(loadedPlugins.size() + " plugins enabled.");

    }

}
