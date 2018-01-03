package net.twasi.core.plugin.api;

import net.twasi.core.plugin.api.events.*;

import java.util.List;

public interface TwasiPluginInterface {

    void onActivate(TwasiActivateEvent e);

    void onDeactivate(TwasiDeactivateEvent e);

    void onEnable(TwasiEnableEvent e);

    void onDisable(TwasiDisableEvent e);

    void onInstall(TwasiInstallEvent e);

    void onUninstall(TwasiUninstallEvent e);

    void onCommand(TwasiCommandEvent e);

    void onMessage(TwasiMessageEvent e);

    List<String> getRegisteredCommands();

}
