package net.twasi.core.events;

import org.pircbotx.hooks.Event;

public class IncomingRawEvent extends TwasiEvent {
    private Event event;

    public IncomingRawEvent(Event event) {
        this.event = event;
    }

    public Event getMessage() {
        return event;
    }
}
