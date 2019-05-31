package net.twasi.core.events;

import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;

public class CommandExecutedEvent extends TwasiEvent {

    private TwasiCommand command;
    private TwasiUserPlugin handlingPlugin;

    public CommandExecutedEvent(TwasiCommand command, TwasiUserPlugin handlingPlugin) {
        this.command = command;
        this.handlingPlugin = handlingPlugin;
    }

    public TwasiCommand getCommand() {
        return command;
    }

    public TwasiUserPlugin getHandlingPlugin() {
        return handlingPlugin;
    }
}
