package net.twasi.core.plugin.api;

import net.twasi.core.models.Message.Command;
import net.twasi.core.models.Message.Message;

import java.util.List;

public interface TwasiPluginInterface {

    void onEnable();

    void onDisable();

    void onCommand(Command command);

    void onMessage(Message msg);

    List<String> getRegisteredCommands();

}
