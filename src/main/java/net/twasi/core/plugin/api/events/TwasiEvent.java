package net.twasi.core.plugin.api.events;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public class TwasiEvent {

    private TwasiUserPlugin userPlugin;

    TwasiEvent(TwasiUserPlugin userPlugin) {
        this.userPlugin = userPlugin;
    }

    public TwasiUserPlugin getUserPlugin() {
        return userPlugin;
    }
}
