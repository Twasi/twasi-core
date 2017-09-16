package net.twasi.core.interfaces;

import net.twasi.core.interfaces.api.TwasiInterface;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

final public class TwasiInterfaceLoader extends URLClassLoader {
    final TwasiInterface twasiInterface;

    TwasiInterfaceLoader(final ClassLoader parent, final File file) throws Exception {
        super(new URL[]{file.toURI().toURL()}, parent);

        // Check for interface.yml
        URL infoYamlUrl = this.getResource("interface.yml");
        if (infoYamlUrl == null) {
            throw new Exception("Cannot load Interface " + file.getAbsolutePath() + ": Invalid or non-existent interface.yml");
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

            Class<? extends TwasiInterface> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(TwasiInterface.class);
            } catch (ClassCastException ex) {
                throw new Exception("Main class `" + main + "' does not extend TwasiInterface", ex);
            }

            twasiInterface = pluginClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new Exception("No public constructor found", e);
        } catch (InstantiationException e) {
            throw new Exception("Abnormal TwasiInterface type", e);
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
