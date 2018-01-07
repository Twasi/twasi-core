package net.twasi.core.plugin.api.events;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TwasiDisableEvent extends TwasiEvent {
    public TwasiDisableEvent(TwasiUserPlugin userPlugin) {
        super(userPlugin);
    }
}
