package net.twasi.core.events;

import net.twasi.core.interfaces.api.TwasiInterface;

public class NewInstanceEvent extends TwasiEvent {
    private TwasiInterface twasiInterface;

    public NewInstanceEvent(TwasiInterface twasiInterface) {
        this.twasiInterface = twasiInterface;
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }
}
