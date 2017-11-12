package net.twasi.core.plugin.api;

import net.twasi.core.plugin.PluginConfig;

import java.util.List;

public abstract class TwasiPlugin implements TwasiPluginInterface {

    protected PluginConfig config;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void setConfig(PluginConfig config) {
        this.config = config;
    }

    public PluginConfig getConfig() {
        return config;
    }

    public List<String> getRegisteredCommands() {
        return null;
    }
}
