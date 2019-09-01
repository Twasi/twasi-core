package net.twasi.core.plugin;

import org.apache.log4j.Level;
import org.apache.log4j.lf5.LogLevel;

public abstract class TwasiPluginConfiguration {

    public PluginLoggingConfiguration LOGGING = new PluginLoggingConfiguration();

    public static class PluginLoggingConfiguration {
        public String PREFIX = null;
        public Level LEVEL = LogLevel.INFO;
    }

}
