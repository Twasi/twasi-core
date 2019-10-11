package net.twasi.core.plugin.api.javascript.models;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;

public abstract class JSTwasiUserPlugin extends TwasiUserPlugin {

    @Override
    public abstract void onCommand(TwasiCommandEvent e);
}
