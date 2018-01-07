package net.twasi.core.plugin.api.events;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TwasiInstallEvent extends TwasiEvent {
    public TwasiInstallEvent(TwasiUserPlugin userPlugin) {
        super(userPlugin);
    }
}
