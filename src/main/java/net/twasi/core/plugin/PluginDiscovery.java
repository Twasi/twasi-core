package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.java.JavaPluginLoader;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.ApiSchemaManagementService;
import net.twasi.core.services.providers.PluginManagerService;

import java.io.File;
import java.util.*;

public class PluginDiscovery {
    private JavaPluginLoader loader = new JavaPluginLoader();

    private Map<File, String> unresolvedDependencyPlugins;
    private List<String> loadedPlugins;

    public void discoverAll() {
        if (!new File("plugins").isDirectory()) {
            TwasiLogger.log.info("Plugins directory not found, creating.");
            if (!new File("plugins").mkdir()) TwasiLogger.log.error("Could not create plugins directory.");
        }

        File[] pluginJars = new File("plugins").listFiles((dir, name) -> name.endsWith(".jar"));

        unresolvedDependencyPlugins = new HashMap<>();
        loadedPlugins = new ArrayList<>();

        assert pluginJars != null;
        for (File pluginFile : pluginJars) {
            loadPlugin(pluginFile);
        }

        TwasiLogger.log.info(ServiceRegistry.get(PluginManagerService.class).getPlugins().size() + " plugin(s), " + ServiceRegistry.get(PluginManagerService.class).getDependencies().size() + " dependency(/ies) loaded.");
        TwasiLogger.log.info("List of loaded plugins: " + Arrays.toString(
                ServiceRegistry.get(PluginManagerService.class).getPlugins().stream().map(plugin -> plugin.getDescription().getName()).toArray()
        ).replaceAll(",\"", ", ").replaceAll("\"", ""));

        TwasiLogger.log.info("List of loaded dependencies: " + Arrays.toString(
                ServiceRegistry.get(PluginManagerService.class).getDependencies().stream().map(plugin -> plugin.getDescription().getName()).toArray()
        ).replaceAll(",\"", ", ").replaceAll("\"", ""));

        TwasiLogger.log.warn("List of plugins that have unsatisfied dependencies: " + Arrays.toString(
                this.unresolvedDependencyPlugins.values().toArray(new String[0])
        ).replaceAll(",\"", ", ").replaceAll("\"", ""));

        ServiceRegistry.get(ApiSchemaManagementService.class).executeBuild();
    }

    private boolean loadPlugin(File pluginFile) {
        TwasiPlugin plugin;
        try {
            PluginConfig description = loader.getPluginConfig(pluginFile);

            List<String> dependencies = description.dependencies;
            if (dependencies == null) dependencies = new ArrayList<String>();
            if (dependencies.stream().anyMatch(dep -> !loadedPlugins.contains(dep.toLowerCase()))) {
                if (!unresolvedDependencyPlugins.containsKey(pluginFile))
                    unresolvedDependencyPlugins.put(pluginFile, description.name);
                return false;
            }

            plugin = loader.loadPlugin(pluginFile, description);

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

            if (plugin.getDescription().isDependency()) {
                ServiceRegistry.get(PluginManagerService.class).registerDependency((TwasiDependency) plugin);
            } else {
                ServiceRegistry.get(PluginManagerService.class).registerPlugin(plugin);
            }
        } catch (Exception e) {
            TwasiLogger.log.error("Error while loading plugin " + pluginFile.getName() + " - is it up to date?", e);
            return false;
        }

        this.loadedPlugins.add(plugin.getDescription().name.toLowerCase()); // Add to loaded plugins to allow dependency check for other plugins

        this.unresolvedDependencyPlugins.remove(pluginFile); // Remove if it failed earlier but doesn't fail now

        this.unresolvedDependencyPlugins.keySet().forEach(this::loadPlugin); // Check other plugins if they can be loaded now
        return true;
    }

}
