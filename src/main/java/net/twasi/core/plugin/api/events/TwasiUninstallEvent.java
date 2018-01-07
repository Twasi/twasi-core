package net.twasi.core.plugin.api.events;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TwasiUninstallEvent extends TwasiEvent {
    public TwasiUninstallEvent(TwasiUserPlugin userPlugin) {
        super(userPlugin);
    }
}
