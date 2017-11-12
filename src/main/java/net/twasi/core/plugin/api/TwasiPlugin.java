package net.twasi.core.plugin.api;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.Command;
import net.twasi.core.plugin.PluginConfig;

import java.util.List;

public abstract class TwasiPlugin implements TwasiPluginInterface {

    protected PluginConfig config;

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onCommand(Command command) {
        TwasiLogger.log.debug("Plugin '" + getConfig().getName() + "' has registered command '" + command.getCommandName() + "' but has no handler.");
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
