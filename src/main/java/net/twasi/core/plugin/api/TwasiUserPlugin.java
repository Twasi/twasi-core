package net.twasi.core.plugin.api;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.events.*;
import net.twasi.core.translations.TwasiTranslation;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiUserPlugin implements TwasiUserPluginInterface {

    private transient TwasiPlugin corePlugin;

    private transient TwasiInterface twasiInterface;
    private transient TwasiTranslation translations;

    public void onEnable(TwasiEnableEvent e) {
    }

    public void onDisable(TwasiDisableEvent e) {
    }

    public void onInstall(TwasiInstallEvent e) {
    }

    public void onUninstall(TwasiInstallEvent e) {
    }

    public void onCommand(TwasiCommandEvent e) {
        TwasiCustomCommand command = null;
        for (TwasiCustomCommand cmd : getCommands())
            if (cmd.getCommandName().equalsIgnoreCase(e.getCommand().getCommandName())) {
                command = cmd;
                break;
            }
        if (command == null)
            TwasiLogger.log.debug("Plugin '" + corePlugin.getDescription().getName() + "' has registered command '" + e.getCommand().getCommandName() + "' but has no handler.");
        else command.process(new TwasiCustomCommandEvent(e.getCommand()));
    }

    public void onMessage(TwasiMessageEvent e) {
        TwasiLogger.log.debug("Plugin '" + corePlugin.getDescription().getName() + "' has registered to message events but has no handler.");
    }

    public List<String> getRegisteredCommands() {
        return null;
    }

    public void setTranslations(TwasiTranslation translations) {
        this.translations = translations;
    }

    public TwasiTranslation getTranslations() {
        return translations;
    }

    public void setCorePlugin(TwasiPlugin corePlugin) {
        this.corePlugin = corePlugin;
    }

    public TwasiPlugin getCorePlugin() {
        return corePlugin;
    }

    public void setTwasiInterface(TwasiInterface twasiInterface) {
        this.twasiInterface = twasiInterface;
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }

    public List<TwasiVariable> getVariables() {
        return new ArrayList<>();
    }

    public List<TwasiCustomCommand> getCommands() {
        return new ArrayList<>();
    }

    public String getTranslation(String key, Object... objects) {
        return getTranslations().getTranslation(getTwasiInterface().getStreamer().getUser(), key, objects);
    }
}
