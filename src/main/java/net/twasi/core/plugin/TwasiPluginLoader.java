package net.twasi.core.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.twasi.core.config.ConfigCatalog.ConfigCatalog;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

final public class TwasiPluginLoader extends URLClassLoader {
        final TwasiPlugin plugin;

        TwasiPluginLoader(final ClassLoader parent, final File file) throws Exception {
            super(new URL[]{file.toURI().toURL()}, parent);

            // Check for plugin.yml
            URL infoYamlUrl = this.getResource("plugin.yml");
            if (infoYamlUrl == null) {
                throw new Exception("Cannot load plugin " + file.getAbsolutePath() + ": Invalid or non-existent plugin.yml");
            }

            // Get File, read
            InputStream stream = (InputStream) infoYamlUrl.getContent();
            PluginConfig config = null;

            // Parse
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            try {
                config = mapper.readValue(stream, PluginConfig.class);
            } catch (Exception e) {
                TwasiLogger.log.error("Cannot parse config file: " + e.getMessage());
            }

            try {
                Class<?> jarClass;
                try {
                    jarClass = Class.forName(config.main, true, this);
                } catch (ClassNotFoundException ex) {
                    throw new Exception("Cannot find main class `" + config.main + "'", ex);
                }

                Class<? extends TwasiPlugin> pluginClass;
                try {
                    pluginClass = jarClass.asSubclass(TwasiPlugin.class);
                } catch (ClassCastException ex) {
                    throw new Exception("Main class `" + config.main + "' does not extend TwasiPlugin", ex);
                }

                plugin = pluginClass.newInstance();
                plugin.setConfig(config);
            } catch (IllegalAccessException e) {
                throw new Exception("No public constructor found", e);
            } catch (InstantiationException e) {
                throw new Exception("Abnormal plugin type", e);
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                return Class.forName(name);
            } catch (Exception e) {
                return super.findClass(name);
            }
        }
}
