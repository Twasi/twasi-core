package net.twasi.core.plugin.api;

import java.util.List;

public interface TwasiPluginInterface {

    void onEnable();

    void onDisable();

    List<String> getRegisteredCommands();

}
