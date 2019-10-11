package net.twasi.core.graphql.model;

import net.twasi.core.plugin.Plugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommand;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;

public class PluginCommandDTO {

    private String commandName;
    private String providingPlugin;
    private boolean listed;
    private boolean timer;

    public PluginCommandDTO(String commandName, String providingPlugin, boolean listed, boolean timer) {
        this.commandName = commandName;
        this.providingPlugin = providingPlugin;
        this.listed = listed;
        this.timer = timer;
    }

    public PluginCommandDTO(TwasiPluginCommand cmd) {
        this(cmd.getCommandName(), cmd.getProvidingUserPlugin().getCorePlugin().getName(), cmd.allowsListing(), cmd.allowsTimer());
    }

    public String getCommandName() {
        return commandName;
    }

    public String getProvidingPlugin() {
        return providingPlugin;
    }

    public boolean isListed() {
        return listed;
    }

    public boolean isTimer() {
        return timer;
    }
}
