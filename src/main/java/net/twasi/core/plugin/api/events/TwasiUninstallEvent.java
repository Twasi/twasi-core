package net.twasi.core.plugin.api.events;

import net.twasi.core.interfaces.api.TwasiInterface;

public class TwasiUninstallEvent {

    private TwasiInterface twasiInterface;

    public TwasiUninstallEvent(TwasiInterface twasiInterface) {
        this.twasiInterface = twasiInterface;
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }

}
