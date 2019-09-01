package net.twasi.core.plugin;

import org.apache.log4j.Level;

public abstract class TwasiPluginConfiguration {

    public PluginLoggingConfiguration LOGGING = new PluginLoggingConfiguration();

    public static class PluginLoggingConfiguration {
        public String PREFIX = null;
        public Level LEVEL = Level.INFO;
    }

}
