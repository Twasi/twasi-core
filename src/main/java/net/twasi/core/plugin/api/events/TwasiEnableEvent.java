package net.twasi.core.plugin.api.events;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TwasiEnableEvent extends TwasiEvent {
    public TwasiEnableEvent(TwasiUserPlugin userPlugin) {
        super(userPlugin);
    }
}
