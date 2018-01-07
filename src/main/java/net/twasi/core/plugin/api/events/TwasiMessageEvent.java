package net.twasi.core.plugin.api.events;

import net.twasi.core.models.Message.TwasiMessage;

public class TwasiMessageEvent {

    private TwasiMessage message;

    public TwasiMessageEvent(TwasiMessage message) {
        this.message = message;
    }

    public TwasiMessage getTwasiInterface() {
        return message;
    }

    public TwasiMessage getMessage() {
        return message;
    }
}
