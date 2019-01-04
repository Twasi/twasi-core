package net.twasi.core.events;

import net.twasi.core.models.Message.TwasiMessage;

public class IncomingMessageEvent extends TwasiEvent {
    private TwasiMessage message;

    public IncomingMessageEvent(TwasiMessage message) {
        this.message = message;
    }

    public TwasiMessage getMessage() {
        return message;
    }

}
