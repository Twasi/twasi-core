package net.twasi.core.events;

import net.twasi.core.models.Message.TwasiMessage;

public class OutgoingMessageEvent extends TwasiEvent {
    private String message;
    private TwasiMessage replyTo;

    public OutgoingMessageEvent(String message, TwasiMessage replyTo) {
        this.message = message;
        this.replyTo = replyTo;
    }

    public String getMessage() {
        return message;
    }

    public TwasiMessage getReplyTo() {
        return replyTo;
    }
}
