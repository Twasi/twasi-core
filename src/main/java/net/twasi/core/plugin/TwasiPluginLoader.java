package net.twasi.core.plugin;

import net.twasi.core.plugin.api.TwasiPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

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
            Yaml yaml = new Yaml();
            HashMap config = yaml.loadAs( stream, HashMap.class);
            String main = (String) config.get("Main");

            try {
                Class<?> jarClass;
                try {
                    jarClass = Class.forName(main, true, this);
                } catch (ClassNotFoundException ex) {
                    throw new Exception("Cannot find main class `" + main + "'", ex);
                }

                Class<? extends TwasiPlugin> pluginClass;
                try {
                    pluginClass = jarClass.asSubclass(TwasiPlugin.class);
                } catch (ClassCastException ex) {
                    throw new Exception("main class `" + main + "' does not extend TwasiPlugin", ex);
                }

                plugin = pluginClass.newInstance();
            } catch (IllegalAccessException ex) {
                throw new Exception("No public constructor", ex);
            } catch (InstantiationException ex) {
                throw new Exception("Abnormal plugin type", ex);
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
