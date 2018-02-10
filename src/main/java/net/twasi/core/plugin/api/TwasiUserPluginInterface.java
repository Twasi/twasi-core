package net.twasi.core.plugin.api;

import net.twasi.core.plugin.api.events.*;

import java.util.List;

public interface TwasiUserPluginInterface {
    /**
     * Called before a plugin will receive onMessage / onCommand events
     *
     * @param e Event data
     */
    void onEnable(TwasiEnableEvent e);

    /**
     * Called before the application wants to stop the instance
     *
     * @param e Event data
     */
    void onDisable(TwasiDisableEvent e);

    /**
     * Called when the user wants to install this plugin.
     *
     * @param e Event data
     */
    void onInstall(TwasiInstallEvent e);

    /**
     * Called when the user wants to uninstall this plugin.
     *
     * @param e Event data
     */
    void onUninstall(TwasiInstallEvent e);

    /**
     * Called when a command is written in the chat
     * Only for commands that are registered in plugin.yml
     *
     * @param e Event data
     */
    void onCommand(TwasiCommandEvent e);

    /**
     * Called when a message is written in the chat
     * Only if messageHandler is registered in plugin.yml
     *
     * @param e Event data
     */
    void onMessage(TwasiMessageEvent e);

    /**
     * Returns all registered commands
     *
     * @return a list of registered commands.
     */
    List<String> getRegisteredCommands();
}
