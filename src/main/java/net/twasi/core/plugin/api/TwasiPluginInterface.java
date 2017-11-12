package net.twasi.core.plugin.api;

import net.twasi.core.models.Message.Command;

import java.util.List;

public interface TwasiPluginInterface {

    void onEnable();

    void onDisable();

    void onCommand(Command command);

    List<String> getRegisteredCommands();

}
