package net.twasi.core.interfaces;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.api.TwasiPlugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

class InterfaceLoader {

    private ArrayList<TwasiInterface> loadedInterfaces = new ArrayList<>();

    InterfaceLoader() {

        if (!new File("interfaces").isDirectory()) {
            TwasiLogger.log.info("Interfaces directory not found, creating.");
            if (!new File("interfaces").mkdir()) TwasiLogger.log.error("Could not create plugins directory.");
        }

        File[] pluginJars = new File("interfaces").listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        assert pluginJars != null;
        for(File plugin : pluginJars) {
            TwasiLogger.log.debug("Loading interface " + plugin);

            try {
                TwasiInterfaceLoader urlCl = new TwasiInterfaceLoader(System.class.getClassLoader(), plugin);
                loadedInterfaces.add(urlCl.twasiInterface);
            } catch (Exception e) {
                TwasiLogger.log.error(e);
            }
        }
        TwasiLogger.log.info(loadedInterfaces.size() + " interface(s) loaded.");

        TwasiLogger.log.info("Enabling interface(s)");
        for (TwasiInterface twasiInterface : loadedInterfaces) {
            twasiInterface.onEnable();
        }
        TwasiLogger.log.info(loadedInterfaces.size() + " interfaces enabled.");

    }

}
