package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

class PluginLoader {

    private ArrayList<TwasiPlugin> loadedPlugins = new ArrayList<>();

    PluginLoader() {

        if (!new File("plugins").isDirectory()) {
            TwasiLogger.log.info("Plugins directory not found, creating.");
            if (!new File("plugins").mkdir()) TwasiLogger.log.error("Could not create plugins directory.");
        }

        File[] pluginJars = new File("plugins").listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        assert pluginJars != null;
        for(File plugin : pluginJars) {
            TwasiLogger.log.debug("Loading plugin " + plugin);

            try {
                TwasiPluginLoader urlCl = new TwasiPluginLoader(System.class.getClassLoader(), plugin);
                loadedPlugins.add(urlCl.plugin);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
            }
        }
        TwasiLogger.log.info(loadedPlugins.size() + " plugin(s) loaded.");

        TwasiLogger.log.info("Enabling plugin(s)");
        for (TwasiPlugin plugin : loadedPlugins) {
            plugin.onEnable();
        }
        TwasiLogger.log.info(loadedPlugins.size() + " plugins enabled.");

    }

}
