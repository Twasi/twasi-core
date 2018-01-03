package net.twasi.core.plugin.api.events;

import net.twasi.core.interfaces.api.TwasiInterface;

public class TwasiInstallEvent {

    private TwasiInterface twasiInterface;

    public TwasiInstallEvent(TwasiInterface twasiInterface) {
        this.twasiInterface = twasiInterface;
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }
}
