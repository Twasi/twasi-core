package net.twasi.core.plugin.api;

import java.util.List;

public abstract class TwasiPlugin implements TwasiPluginInterface {

    public void onEnable() {

    }

    public void onDisable() {

    }

    public String getName() {
        // Read name out of plugin.yml
        return "tbd";
    }

    public List<String> getRegisteredCommands() {
        return null;
    }
}
