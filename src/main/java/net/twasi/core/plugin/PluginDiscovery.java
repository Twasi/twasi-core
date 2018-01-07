package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.java.JavaPluginLoader;
import net.twasi.core.services.PluginManagerService;

import java.io.File;
import java.util.Arrays;

public class PluginDiscovery {
    JavaPluginLoader loader = new JavaPluginLoader();

    public void discoverAll() {
        if (!new File("plugins").isDirectory()) {
            TwasiLogger.log.info("Plugins directory not found, creating.");
            if (!new File("plugins").mkdir()) TwasiLogger.log.error("Could not create plugins directory.");
        }

        File[] pluginJars = new File("plugins").listFiles((dir, name) -> name.endsWith(".jar"));

        assert pluginJars != null;
        for(File pluginFile : pluginJars) {
            Plugin plugin;
            try {
                plugin = loader.loadPlugin(pluginFile);
                loader.enablePlugin(plugin);
                PluginManagerService.getService().registerPlugin((TwasiPlugin) plugin);
            } catch (Exception e) {
                TwasiLogger.log.error("Error while loading plugin " + pluginFile.getName() + " - is it up to date?");
                e.printStackTrace();
            }
        }

        TwasiLogger.log.info(PluginManagerService.getService().getPlugins().size() + " plugin(s) loaded.");
        TwasiLogger.log.info("List of loaded plugins: " + Arrays.toString(
                PluginManagerService.getService().getPlugins().stream().map(plugin -> plugin.getDescription().getName()).toArray()
        ));
    }

}
