package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.services.PluginManagerService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
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
            TwasiPluginLoader urlCl = null;

            try {
                urlCl = new TwasiPluginLoader(System.class.getClassLoader(), plugin);
                PluginManagerService.getService().registerPlugin(urlCl.plugin);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
            } finally {
                if (urlCl != null) {
                    try {
                        urlCl.close();
                    } catch (IOException e) {
                        TwasiLogger.log.error(e);
                    }
                }
            }
        }
        TwasiLogger.log.info(PluginManagerService.getService().getPlugins().size() + " plugin(s) loaded.");
        TwasiLogger.log.info("List of loaded plugins: " + Arrays.toString(
                PluginManagerService.getService().getPlugins().stream().map(plugin -> plugin.getConfig().getName()).toArray()
        ));
    }

}
