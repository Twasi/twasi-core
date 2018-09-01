package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.java.JavaPluginLoader;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.ApiSchemaManagementService;
import net.twasi.core.services.providers.PluginManagerService;

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
        for (File pluginFile : pluginJars) {
            Plugin plugin;
            try {
                plugin = loader.loadPlugin(pluginFile);

                // Register API, if necessary
                if (plugin.getDescription().getApi() != null) {
                    if (plugin.getGraphQLResolver() == null) {
                        TwasiLogger.log.error("Plugin " + plugin.getName() + " has registered api, but does not provide a resolver. API registration skipped.");
                    } else {
                        ServiceRegistry.get(ApiSchemaManagementService.class)
                                .addForPlugin(plugin.getName(), plugin.getDescription().getApi(), plugin.getGraphQLResolver());
                    }
                }

                loader.enablePlugin(plugin);
                ServiceRegistry.get(PluginManagerService.class).registerPlugin((TwasiPlugin) plugin);
            } catch (Exception e) {
                TwasiLogger.log.error("Error while loading plugin " + pluginFile.getName() + " - is it up to date?");
                e.printStackTrace();
            }
        }

        TwasiLogger.log.info(ServiceRegistry.get(PluginManagerService.class).getPlugins().size() + " plugin(s) loaded.");
        TwasiLogger.log.info("List of loaded plugins: " + Arrays.toString(
                ServiceRegistry.get(PluginManagerService.class).getPlugins().stream().map(plugin -> plugin.getDescription().getName()).toArray()
        ));

        ServiceRegistry.get(ApiSchemaManagementService.class).executeBuild();
    }

}
