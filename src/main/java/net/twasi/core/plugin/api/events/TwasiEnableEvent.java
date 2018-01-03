package net.twasi.core.plugin.api.events;

import net.twasi.core.interfaces.api.TwasiInterface;

public class TwasiEnableEvent {

    private TwasiInterface twasiInterface;

    public TwasiEnableEvent(TwasiInterface twasiInterface) {
        this.twasiInterface = twasiInterface;
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }
}
