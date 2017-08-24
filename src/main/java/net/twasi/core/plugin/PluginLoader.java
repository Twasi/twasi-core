package net.twasi.core.plugin;

import net.twasi.core.logger.TwasiLogger;

import java.io.File;
import java.io.FilenameFilter;

public class PluginLoader {



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
            System.out.println(plugin.getAbsolutePath());
        }

    }

}
