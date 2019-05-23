package net.twasi.core.plugin.api;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;
import net.twasi.core.plugin.api.events.*;
import net.twasi.core.translations.TwasiTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TwasiUserPlugin implements TwasiUserPluginInterface {

    private transient TwasiPlugin corePlugin;

    private transient TwasiInterface twasiInterface;
    private transient TwasiTranslation translations;

    private List<TwasiPluginCommand> commands = new ArrayList<>();
    private List<TwasiVariable> variables = new ArrayList<>();

    public void onEnable(TwasiEnableEvent e) {
    }

    public void onDisable(TwasiDisableEvent e) {
    }

    public void onInstall(TwasiInstallEvent e) {
    }

    public void onUninstall(TwasiInstallEvent e) {
    }

    public void onCommand(TwasiCommandEvent e) {
        TwasiPluginCommand command = null;
        String name = e.getCommand().getCommandName().toLowerCase();
        for (TwasiPluginCommand cmd : getCommands())
            if (cmd.getCommandName().equalsIgnoreCase(name) || cmd.getAliases().stream().map(String::toLowerCase).collect(Collectors.toList()).contains(name)) {
                command = cmd;
                break;
            }
        if (command == null)
            TwasiLogger.log.debug("Plugin '" + corePlugin.getDescription().getName() + "' has registered command '" + e.getCommand().getCommandName() + "' but has no handler.");
        else command.processInternal(e.getCommand(), getCorePlugin().getClassLoader());
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
        return variables;
    }

    protected void registerCommand(Class<? extends TwasiPluginCommand> clazz) {
        try {
            commands.add(clazz.getDeclaredConstructor(TwasiUserPlugin.class).newInstance(this));
        } catch (Exception e) {
            TwasiLogger.log.warn("Command class " + clazz.getName() + " of plugin " + getCorePlugin().getDescription().getName() + " could not be instantiated.");
        }
    }

    protected void registerCommand(TwasiPluginCommand command) {
        commands.add(command);
    }


    protected void registerVariable(Class<? extends TwasiVariable> clazz) {
        try {
            variables.add(clazz.getDeclaredConstructor(TwasiUserPlugin.class).newInstance(this));
        } catch (Exception e) {
            TwasiLogger.log.warn("Variable class " + clazz.getName() + " of plugin " + getCorePlugin().getDescription().getName() + " could not be instantiated.");
        }
    }

    protected void registerVariable(TwasiVariable command) {
        variables.add(command);
    }

    public List<TwasiPluginCommand> getCommands() {
        return commands;
    }

    @Deprecated
    public final String getTranslation(String key, Object... objects) {
        return getTranslations().getTranslation(getTwasiInterface().getStreamer().getUser(), key, objects);
    }

    @Deprecated
    public final String getRandomTranslation(String key, Object... objects) {
        return getTranslations().getRandomTranslation(getTwasiInterface().getStreamer().getUser(), key, objects);
    }
}
