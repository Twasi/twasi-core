package net.twasi.core.plugin.api.customcommands;

import net.twasi.core.plugin.api.TwasiUserPlugin;

public abstract class TwasiPluginCommand extends TwasiCustomCommand {

    private TwasiUserPlugin twasiUserPlugin;

    public TwasiPluginCommand(TwasiUserPlugin twasiUserPlugin) {
        this.twasiUserPlugin = twasiUserPlugin;
    }

    @Override
    public boolean allowsTimer() {
        return true;
    }

    @Override
    public boolean allowsListing() {
        return true;
    }

    protected final String getTranslation(String key, Object... objects) {
        return this.twasiUserPlugin.getTranslation(key, objects);
    }

    protected final String getRandomTranslation(String key, Object... objects) {
        return this.twasiUserPlugin.getRandomTranslation(key, objects);
    }
}
