package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiPluginInterface;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

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
    }

}
