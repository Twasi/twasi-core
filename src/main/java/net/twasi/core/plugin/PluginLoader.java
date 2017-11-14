package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.java.JavaPluginLoader;
import net.twasi.core.services.PluginManagerService;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

class PluginLoader {

    PluginLoader() {

        if (!new File("plugins").isDirectory()) {
            TwasiLogger.log.info("Plugins directory not found, creating.");
            if (!new File("plugins").mkdir()) TwasiLogger.log.error("Could not create plugins directory.");
        }

        File[] pluginJars = new File("plugins").listFiles((dir, name) -> name.endsWith(".jar"));

        assert pluginJars != null;
        for(File plugin : pluginJars) {
            TwasiLogger.log.debug("Loading plugin " + plugin);

            try {
                JavaPluginLoader pluginLoader = new JavaPluginLoader(plugin);
                PluginManagerService.getService().registerPlugin(pluginLoader.plugin);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
                e.printStackTrace();
            }
        }
        TwasiLogger.log.info(PluginManagerService.getService().getPlugins().size() + " plugin(s) loaded.");
        TwasiLogger.log.info("List of loaded plugins: " + Arrays.toString(
                PluginManagerService.getService().getPlugins().stream().map(plugin -> plugin.getConfig().getName()).toArray()
        ));
    }

}
