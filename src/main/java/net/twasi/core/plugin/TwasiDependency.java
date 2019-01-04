package net.twasi.core.plugin;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public abstract class TwasiDependency extends TwasiPlugin {
    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return null;
    }

    @Override
    public boolean isDependency() {
        return true;
    }
}
