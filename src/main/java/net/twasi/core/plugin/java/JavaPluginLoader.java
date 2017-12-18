package net.twasi.core.plugin.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.translations.TwasiTranslation;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class JavaPluginLoader {

    private TwasiPlugin plugin;

    public JavaPluginLoader (File file) {
        URLClassLoader cl = null;
        try {
            URL[] urls = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};
            cl = URLClassLoader.newInstance(urls);

            // Check for plugin.yml
            URL infoYamlUrl = cl.getResource("plugin.yml");
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
            } catch (Exception ee) {
                TwasiLogger.log.error("Cannot parse config file: " + ee.getMessage());
            }

            if (config == null) {
                throw new Exception("Cannot parse config: Config is NULL.");
            }

            Class<?> jarClass;
            try {
                jarClass = Class.forName(config.main, true, cl);
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
            plugin.setTranslations(new TwasiTranslation(cl));
        } catch (Exception e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
        }
    }

    public TwasiPlugin getPlugin() {
        return plugin;
    }

}
