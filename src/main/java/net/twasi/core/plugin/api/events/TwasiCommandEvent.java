package net.twasi.core.plugin.api.events;

import net.twasi.core.models.Message.TwasiCommand;

public class TwasiCommandEvent {

    TwasiCommand command;

    public TwasiCommandEvent(TwasiCommand command) {
        this.command = command;
    }

    public TwasiCommand getCommand() {
        return command;
    }
}
